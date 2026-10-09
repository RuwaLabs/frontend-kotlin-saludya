package pe.edu.upc.saludya.iam.presentation.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.*
import java.security.MessageDigest
import javax.inject.Inject

data class PasswordResetUiState(
    val linkInput: String = "", val validLink: Boolean = false,
    val password: String = "", val confirmation: String = "",
    val passwordInvalid: Boolean = false, val mismatch: Boolean = false,
    val busy: Boolean = false, val updated: Boolean = false, val failure: AccessFailure? = null
) {
    override fun toString(): String = "PasswordResetUiState[REDACTED]"
}

@HiltViewModel
class PasswordResetViewModel @Inject constructor(
    private val repository: RecoveryActionsRepository, private val links: PasswordResetLink,
    private val sessions: PatientSessionStore
) : ViewModel() {
    private val mutableState = MutableStateFlow(PasswordResetUiState())
    val state = mutableState.asStateFlow()
    private var token: String? = null
    private var fingerprint: List<Byte>? = null
    private var request: Job? = null

    fun acceptLink(value: String) {
        val parsed = links.token(value)
        val digest = parsed?.let { MessageDigest.getInstance("SHA-256").digest(it.toByteArray()).toList() }
        if (digest != null && fingerprint == digest) return
        request?.cancel()
        token = parsed
        fingerprint = digest
        mutableState.value = PasswordResetUiState(validLink = parsed != null,
            failure = if (parsed == null) AccessFailure.RESET_LINK_INVALID else null)
    }
    fun editLink(value: String) {
        if (!mutableState.value.busy) {
            token = null; fingerprint = null
            mutableState.value = PasswordResetUiState(linkInput = value)
        }
    }
    fun openPastedLink() { acceptLink(mutableState.value.linkInput) }
    fun editPassword(value: String) {
        val current = mutableState.value
        if (!current.busy && !current.updated) mutableState.value = current.copy(password = value,
            passwordInvalid = false, mismatch = false, failure = null)
    }
    fun editConfirmation(value: String) {
        val current = mutableState.value
        if (!current.busy && !current.updated) mutableState.value = current.copy(confirmation = value, mismatch = false, failure = null)
    }
    fun reset() {
        if (mutableState.value.busy) return
        token = null; fingerprint = null
        mutableState.value = PasswordResetUiState()
    }
    fun save() {
        val current = mutableState.value
        val recoveryToken = token ?: return
        if (current.busy || current.updated || !current.validLink) return
        val invalid = !AccessValidation.password(current.password)
        val mismatch = current.password != current.confirmation
        if (invalid || mismatch) {
            mutableState.value = current.copy(passwordInvalid = invalid, mismatch = mismatch)
            return
        }
        mutableState.value = current.copy(busy = true, failure = null)
        request = viewModelScope.launch {
            when (val result = repository.resetPassword(recoveryToken, current.password, current.confirmation)) {
                is AccessResult.Success -> {
                    token = null
                    sessions.clear()
                    mutableState.value = PasswordResetUiState(updated = true)
                }
                is AccessResult.Failure -> {
                    val invalidLink = result.reason == AccessFailure.RESET_LINK_INVALID
                    if (invalidLink) token = null
                    mutableState.value = mutableState.value.copy(busy = false, failure = result.reason,
                        validLink = !invalidLink, password = if (invalidLink) "" else current.password,
                        confirmation = if (invalidLink) "" else current.confirmation,
                        mismatch = result.reason == AccessFailure.PASSWORD_MISMATCH)
                }
            }
        }
    }
}
