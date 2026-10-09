package pe.edu.upc.saludya.features.iam.presentation.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.iam.domain.model.AccessValidation
import pe.edu.upc.saludya.features.iam.domain.model.PatientSessionStore
import pe.edu.upc.saludya.features.iam.domain.repository.*
import java.time.Clock
import javax.inject.Inject

enum class LoginNotice { EMAIL_FORMAT, PASSWORD_REQUIRED, CODE_FORMAT, CODE_SENT, SESSION_EXPIRED, LOGOUT_UNCONFIRMED }

data class LoginUiState(
    val email: String = "", val password: String = "", val code: String = "",
    val busy: Boolean = false, val challenge: LoginChallenge? = null,
    val session: PatientSession? = null, val failure: AccessFailure? = null,
    val notice: LoginNotice? = null, val logoutConfirmation: Boolean = false,
    val profile: PatientAccountProfile? = null
) {
    override fun toString(): String = "LoginUiState[REDACTED]"
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AccessRepository, private val sessions: PatientSessionStore, private val clock: Clock
) : ViewModel() {
    private val mutableState = MutableStateFlow(LoginUiState(session = sessions.current()))
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            sessions.expired.collect { expired ->
                if (expired) mutableState.value = LoginUiState(notice = LoginNotice.SESSION_EXPIRED)
            }
        }
    }

    fun editEmail(value: String) { edit { copy(email = value) } }
    fun editPassword(value: String) { edit { copy(password = value) } }
    fun editCode(value: String) { edit { copy(code = value) } }
    private fun edit(change: LoginUiState.() -> LoginUiState) {
        val current = mutableState.value
        if (current.busy || current.session != null) return
        mutableState.value = current.change().copy(failure = null, notice = null)
    }

    fun reset() {
        if (!mutableState.value.busy) {
            val expired = sessions.expired.value
            mutableState.value = LoginUiState(session = sessions.current(),
                notice = if (expired) LoginNotice.SESSION_EXPIRED else null)
        }
    }

    fun cancelChallenge() {
        val current = mutableState.value
        if (!current.busy) mutableState.value = LoginUiState(email = current.email)
    }

    fun login() {
        val current = mutableState.value
        if (current.busy || current.challenge != null || current.session != null) return
        val email = AccessValidation.email(current.email)
        if (email == null || current.password.isEmpty()) {
            mutableState.value = current.copy(notice = if (email == null) LoginNotice.EMAIL_FORMAT else LoginNotice.PASSWORD_REQUIRED)
            return
        }
        mutableState.value = current.copy(email = email, busy = true, notice = null, failure = null)
        viewModelScope.launch {
            when (val result = repository.login(email, current.password)) {
                is AccessResult.Success -> mutableState.value = mutableState.value.copy(busy = false,
                    password = "", challenge = result.value, code = "", notice = LoginNotice.CODE_SENT)
                is AccessResult.Failure -> mutableState.value = mutableState.value.copy(busy = false,
                    password = "", failure = result.reason)
            }
        }
    }

    fun verify() {
        val current = mutableState.value
        val challenge = current.challenge ?: return
        if (current.busy || current.session != null) return
        if (!challenge.expiresAt.isAfter(clock.instant())) {
            mutableState.value = current.copy(failure = AccessFailure.EXPIRED_CODE)
            return
        }
        if (!AccessValidation.code(current.code)) {
            mutableState.value = current.copy(notice = LoginNotice.CODE_FORMAT)
            return
        }
        mutableState.value = current.copy(busy = true, notice = null, failure = null)
        viewModelScope.launch {
            when (val result = repository.verifyLogin(challenge.id, current.code)) {
                is AccessResult.Success -> {
                    if (sessions.open(result.value)) {
                        mutableState.value = LoginUiState(session = result.value)
                    } else mutableState.value = mutableState.value.copy(busy = false, failure = AccessFailure.EXPIRED_CODE)
                }
                is AccessResult.Failure -> mutableState.value = mutableState.value.copy(busy = false, failure = result.reason)
            }
        }
    }

    fun resend() {
        val current = mutableState.value
        val challenge = current.challenge ?: return
        if (current.busy) return
        if (!challenge.expiresAt.isAfter(clock.instant())) {
            mutableState.value = current.copy(failure = AccessFailure.EXPIRED_CODE)
            return
        }
        mutableState.value = current.copy(busy = true, code = "", failure = null, notice = null)
        viewModelScope.launch {
            when (val result = repository.resendLogin(challenge.id)) {
                is AccessResult.Success -> mutableState.value = mutableState.value.copy(busy = false, notice = LoginNotice.CODE_SENT)
                is AccessResult.Failure -> mutableState.value = mutableState.value.copy(busy = false, failure = result.reason)
            }
        }
    }

    // Called only while the authenticated route is displayed; tokens stay in process memory.
    suspend fun observeExpiration() {
        while (mutableState.value.session != null) {
            if (sessions.current() == null) {
                mutableState.value = LoginUiState(notice = LoginNotice.SESSION_EXPIRED)
                return
            }
            delay(1000)
        }
    }

    fun acknowledgeExpiration() {
        if (mutableState.value.notice == LoginNotice.SESSION_EXPIRED) {
            sessions.clear()
            mutableState.value = LoginUiState()
        }
    }

    private fun requireSession(): PatientSession? {
        val session = sessions.current()
        if (session == null) {
            sessions.expire()
            mutableState.value = LoginUiState(notice = LoginNotice.SESSION_EXPIRED)
        }
        return session
    }

    fun requestLogout() {
        val current = mutableState.value
        if (current.busy || current.session == null) return
        requireSession() ?: return
        mutableState.value = current.copy(logoutConfirmation = true)
    }

    fun cancelLogout() {
        val current = mutableState.value
        if (!current.busy) mutableState.value = current.copy(logoutConfirmation = false)
    }

    fun refreshProfile() {
        val current = mutableState.value
        if (current.busy || current.session == null || current.logoutConfirmation) return
        val session = requireSession() ?: return
        mutableState.value = current.copy(busy = true, failure = null)
        viewModelScope.launch {
            val result = repository.profile(session.accessToken)
            if (sessions.current()?.accessToken != session.accessToken) {
                mutableState.value = LoginUiState(notice = LoginNotice.SESSION_EXPIRED)
                return@launch
            }
            mutableState.value = when (result) {
                is AccessResult.Success -> {
                    if (result.value.userId == session.userId && result.value.patientId == session.patientId) {
                        mutableState.value.copy(busy = false, profile = result.value)
                    } else mutableState.value.copy(busy = false, failure = AccessFailure.UNEXPECTED)
                }
                is AccessResult.Failure -> {
                    if (result.reason == AccessFailure.SESSION_EXPIRED || result.reason == AccessFailure.INVALID_CREDENTIALS) {
                        sessions.expireIfToken(session.accessToken)
                        LoginUiState(notice = LoginNotice.SESSION_EXPIRED)
                    } else mutableState.value.copy(busy = false, failure = result.reason)
                }
            }
        }
    }

    fun logout() {
        val current = mutableState.value
        if (current.busy || !current.logoutConfirmation || current.session == null) return
        val session = requireSession() ?: return
        sessions.clear()
        mutableState.value = LoginUiState(busy = true)
        viewModelScope.launch {
            val result = repository.logout(session.accessToken)
            mutableState.value = LoginUiState(notice = if (result is AccessResult.Failure) LoginNotice.LOGOUT_UNCONFIRMED else null)
        }
    }
}
