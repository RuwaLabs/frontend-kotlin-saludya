package pe.edu.upc.saludya.features.configuration.presentation.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.iam.domain.model.AccessValidation
import pe.edu.upc.saludya.features.iam.domain.model.PatientSessionStore
import pe.edu.upc.saludya.features.iam.domain.repository.AccessFailure
import pe.edu.upc.saludya.features.iam.domain.repository.AccessResult
import pe.edu.upc.saludya.features.iam.domain.repository.RecoveryActionsRepository
import pe.edu.upc.saludya.features.iam.infrastructure.remote.AccessApi
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accounts: AccessApi,
    private val recovery: RecoveryActionsRepository,
    private val session: SessionProvider,
    private val sessions: PatientSessionStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private var challengeId: String? = null
    private var resetToken: String? = null

    init {
        loadEmail()
    }

    fun loadEmail() {
        val token = session.accessToken
        if (token.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    step = ChangePasswordStep.ERROR,
                    error = context.getString(R.string.change_password_session_expired),
                )
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(step = ChangePasswordStep.LOADING, error = null) }
            val response = accounts.profile("Bearer $token")
            val email = response.body()?.email
            if (!response.isSuccessful || email.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        step = ChangePasswordStep.ERROR,
                        error = context.getString(R.string.change_password_email_error),
                    )
                }
            } else {
                _uiState.update {
                    it.copy(step = ChangePasswordStep.READY, email = email, error = null)
                }
            }
        }
    }

    fun editCode(value: String) {
        _uiState.update { it.copy(code = value.filter { char -> char.isDigit() }.take(6), error = null) }
    }

    fun editPassword(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun editConfirmation(value: String) {
        _uiState.update { it.copy(confirmation = value, error = null) }
    }

    fun sendCode() {
        val current = _uiState.value
        if (current.busy || current.email.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null, message = null) }
            when (val result = recovery.requestRecoveryCode(current.email)) {
                is AccessResult.Success -> {
                    challengeId = result.value.challengeId
                    _uiState.update {
                        it.copy(
                            step = ChangePasswordStep.CODE_SENT,
                            maskedEmail = result.value.maskedEmail,
                            busy = false,
                            message = context.getString(
                                R.string.change_password_code_sent_message,
                                result.value.maskedEmail,
                            ),
                        )
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    fun verifyCode() {
        val current = _uiState.value
        val challenge = challengeId ?: return
        if (current.busy) return
        if (!AccessValidation.code(current.code)) {
            _uiState.update {
                it.copy(error = context.getString(R.string.change_password_code_required))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null) }
            when (val result = recovery.verifyRecoveryCode(challenge, current.code)) {
                is AccessResult.Success -> {
                    resetToken = result.value.resetToken
                    _uiState.update {
                        it.copy(step = ChangePasswordStep.VERIFIED, busy = false, message = null)
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    fun save() {
        val current = _uiState.value
        val token = resetToken ?: return
        if (current.busy) return
        if (!AccessValidation.password(current.password)) {
            _uiState.update {
                it.copy(error = context.getString(R.string.change_password_password_requirements))
            }
            return
        }
        if (current.password != current.confirmation) {
            _uiState.update {
                it.copy(error = context.getString(R.string.change_password_password_mismatch))
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null) }
            when (val result = recovery.resetPasswordWithGrant(token, current.password, current.confirmation)) {
                is AccessResult.Success -> {
                    sessions.clear()
                    _uiState.update {
                        it.copy(
                            step = ChangePasswordStep.SAVED,
                            busy = false,
                            password = "",
                            confirmation = "",
                        )
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    private fun failureMessage(failure: AccessFailure): String = when (failure) {
        AccessFailure.INVALID_CODE ->
            context.getString(R.string.change_password_invalid_code)

        AccessFailure.EXPIRED_CODE ->
            context.getString(R.string.change_password_expired_code)

        AccessFailure.TOO_MANY_ATTEMPTS ->
            context.getString(R.string.change_password_too_many_attempts)

        AccessFailure.PASSWORD_MISMATCH ->
            context.getString(R.string.change_password_password_mismatch)

        AccessFailure.RESET_LINK_INVALID ->
            context.getString(R.string.change_password_reset_link_invalid)

        AccessFailure.UNAVAILABLE ->
            context.getString(R.string.change_password_server_unavailable)

        else ->
            context.getString(R.string.change_password_operation_error)
    }
}
