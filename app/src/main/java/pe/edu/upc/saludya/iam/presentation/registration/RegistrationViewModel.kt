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
    IDLE, SUBMITTING, ACCEPTED, IDENTITY_REQUIRED, IDENTITY_ALREADY_REGISTERED,
    IDENTITY_MISMATCH, ADULT_REQUIRED, DUPLICATE_RECORD, INVALID_REQUEST, UNAVAILABLE, ERROR
}

data class RegistrationUiState(
    val identity: VerifiedIdentity? = null,
    val form: CredentialsForm = CredentialsForm(),
    val errors: Map<CredentialsField, CredentialsError> = emptyMap(),
    val status: RegistrationStatus = RegistrationStatus.IDLE,
    val receipt: RegistrationReceipt? = null
) {
    val canSubmit: Boolean get() = identity != null && status != RegistrationStatus.SUBMITTING && receipt == null
    val canContinue: Boolean get() = status == RegistrationStatus.ACCEPTED && receipt != null
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

    fun edit(field: CredentialsField, value: String) {
        val current = mutableState.value
        if (current.status == RegistrationStatus.SUBMITTING || current.receipt != null) return
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

    fun submit() {
        val current = mutableState.value
        if (current.status == RegistrationStatus.SUBMITTING || current.receipt != null) return
        val identity = current.identity
        if (identity == null) {
            mutableState.value = current.copy(status = RegistrationStatus.IDENTITY_REQUIRED)
            return
        }
        val validation = CredentialsValidator.validate(current.form)
        val credentials = validation.credentials
        if (credentials == null) {
            mutableState.value = current.copy(errors = validation.errors, status = RegistrationStatus.IDLE)
            return
        }
        mutableState.value = current.copy(errors = emptyMap(), status = RegistrationStatus.SUBMITTING)
        viewModelScope.launch {
            val result = repository.register(identity, credentials)
            val state = mutableState.value
            mutableState.value = when (result) {
                is RegistrationResult.Accepted -> state.copy(
                    status = RegistrationStatus.ACCEPTED,
                    receipt = result.receipt,
                    form = state.form.copy(password = "", confirmPassword = "")
                )
                RegistrationResult.EmailInUse -> state.copy(status = RegistrationStatus.IDLE,
                    errors = mapOf(CredentialsField.EMAIL to CredentialsError.EMAIL_IN_USE))
                RegistrationResult.IdentityAlreadyRegistered -> state.copy(status = RegistrationStatus.IDENTITY_ALREADY_REGISTERED)
                RegistrationResult.IdentityMismatch -> state.copy(status = RegistrationStatus.IDENTITY_MISMATCH)
                RegistrationResult.AdultRequired -> state.copy(status = RegistrationStatus.ADULT_REQUIRED)
                RegistrationResult.DuplicateRecord -> state.copy(status = RegistrationStatus.DUPLICATE_RECORD)
                RegistrationResult.InvalidRequest -> state.copy(status = RegistrationStatus.INVALID_REQUEST)
                RegistrationResult.Unavailable -> state.copy(status = RegistrationStatus.UNAVAILABLE)
                RegistrationResult.UnexpectedError -> state.copy(status = RegistrationStatus.ERROR)
            }
        }
    }
}
