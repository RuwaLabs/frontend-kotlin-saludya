package pe.edu.upc.saludya.iam.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.*
import javax.inject.Inject

enum class RegistrationStatus {
    IDLE, SUBMITTING, CODE_SENT, ACCEPTED, IDENTITY_REQUIRED, IDENTITY_ALREADY_REGISTERED,
    IDENTITY_MISMATCH, ADULT_REQUIRED, DUPLICATE_RECORD, INVALID_REQUEST, UNAVAILABLE, ERROR,
    CODE_FORMAT, INVALID_CODE, EXPIRED_CODE, TOO_MANY_ATTEMPTS
}

data class RegistrationUiState(
    val identity: VerifiedIdentity? = null,
    val form: CredentialsForm = CredentialsForm(),
    val errors: Map<CredentialsField, CredentialsError> = emptyMap(),
    val status: RegistrationStatus = RegistrationStatus.IDLE,
    val receipt: RegistrationReceipt? = null,
    val awaitingCode: Boolean = false,
    val code: String = ""
) {
    val canSubmit: Boolean get() = identity != null && status != RegistrationStatus.SUBMITTING && receipt == null
    val canContinue: Boolean get() = status == RegistrationStatus.ACCEPTED && receipt != null
    override fun toString(): String = "RegistrationUiState[REDACTED]"
}

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val repository: RegistrationRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(RegistrationUiState())
    val state = mutableState.asStateFlow()

    fun prepare(identity: VerifiedIdentity) {
        val current = mutableState.value
        if (current.status == RegistrationStatus.SUBMITTING || current.identity == identity) return
        mutableState.value = RegistrationUiState(identity = identity)
    }

    fun reset() { mutableState.value = RegistrationUiState() }

    fun edit(field: CredentialsField, value: String) {
        val current = mutableState.value
        if (!current.canSubmit || current.awaitingCode) return
        val form = when (field) {
            CredentialsField.EMAIL -> current.form.copy(email = value)
            CredentialsField.PASSWORD -> current.form.copy(password = value)
            CredentialsField.CONFIRM_PASSWORD -> current.form.copy(confirmPassword = value)
            CredentialsField.PHONE -> current.form.copy(phone = value)
        }
        val cleared = if (field == CredentialsField.PASSWORD) {
            current.errors - CredentialsField.PASSWORD - CredentialsField.CONFIRM_PASSWORD
        } else current.errors - field
        mutableState.value = current.copy(form = form, errors = cleared, status = RegistrationStatus.IDLE)
    }

    fun editCode(value: String) {
        val current = mutableState.value
        if (!current.canSubmit || !current.awaitingCode) return
        mutableState.value = current.copy(code = value, status = RegistrationStatus.IDLE)
    }

    fun backToCredentials() {
        val current = mutableState.value
        if (!current.canSubmit) return
        mutableState.value = current.copy(awaitingCode = false, code = "", status = RegistrationStatus.IDLE)
    }

    fun submit() {
        val current = mutableState.value
        if (!current.canSubmit) {
            if (current.identity == null) mutableState.value = current.copy(status = RegistrationStatus.IDENTITY_REQUIRED)
            return
        }
        if (current.awaitingCode) return
        val validation = CredentialsValidator.validate(current.form)
        val credentials = validation.credentials
        if (credentials == null) {
            mutableState.value = current.copy(errors = validation.errors, status = RegistrationStatus.IDLE)
            return
        }
        mutableState.value = current.copy(form = current.form.copy(email = credentials.email),
            errors = emptyMap(), status = RegistrationStatus.SUBMITTING)
        viewModelScope.launch { applyResult(repository.sendCode(credentials.email)) }
    }

    fun resend() {
        val current = mutableState.value
        if (!current.canSubmit || !current.awaitingCode) return
        mutableState.value = current.copy(code = "", status = RegistrationStatus.SUBMITTING)
        viewModelScope.launch { applyResult(repository.sendCode(current.form.email)) }
    }

    fun verify() {
        val current = mutableState.value
        if (!current.canSubmit || !current.awaitingCode) return
        if (!AccessValidation.code(current.code)) {
            mutableState.value = current.copy(status = RegistrationStatus.CODE_FORMAT)
            return
        }
        val identity = current.identity ?: return
        val credentials = CredentialsValidator.validate(current.form).credentials ?: return
        mutableState.value = current.copy(status = RegistrationStatus.SUBMITTING)
        viewModelScope.launch { applyResult(repository.register(identity, credentials, current.code)) }
    }

    private fun applyResult(result: RegistrationResult) {
        val current = mutableState.value
        mutableState.value = when (result) {
            RegistrationResult.CodeSent -> current.copy(awaitingCode = true, status = RegistrationStatus.CODE_SENT, code = "")
            is RegistrationResult.Accepted -> current.copy(status = RegistrationStatus.ACCEPTED, receipt = result.receipt,
                form = current.form.copy(password = "", confirmPassword = ""), code = "")
            RegistrationResult.EmailInUse -> current.copy(awaitingCode = false, code = "", status = RegistrationStatus.IDLE,
                errors = mapOf(CredentialsField.EMAIL to CredentialsError.EMAIL_IN_USE))
            else -> current.copy(status = when (result) {
                RegistrationResult.InvalidCode -> RegistrationStatus.INVALID_CODE
                RegistrationResult.ExpiredCode -> RegistrationStatus.EXPIRED_CODE
                RegistrationResult.TooManyAttempts -> RegistrationStatus.TOO_MANY_ATTEMPTS
                RegistrationResult.IdentityAlreadyRegistered -> RegistrationStatus.IDENTITY_ALREADY_REGISTERED
                RegistrationResult.IdentityMismatch -> RegistrationStatus.IDENTITY_MISMATCH
                RegistrationResult.AdultRequired -> RegistrationStatus.ADULT_REQUIRED
                RegistrationResult.DuplicateRecord -> RegistrationStatus.DUPLICATE_RECORD
                RegistrationResult.InvalidRequest -> RegistrationStatus.INVALID_REQUEST
                RegistrationResult.Unavailable -> RegistrationStatus.UNAVAILABLE
                else -> RegistrationStatus.ERROR
            })
        }
    }
}
