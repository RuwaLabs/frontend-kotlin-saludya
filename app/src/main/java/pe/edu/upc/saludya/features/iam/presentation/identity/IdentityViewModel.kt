package pe.edu.upc.saludya.features.iam.presentation.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.iam.domain.model.*
import pe.edu.upc.saludya.features.iam.domain.repository.IdentityRepository
import pe.edu.upc.saludya.features.iam.domain.repository.IdentityVerificationResult
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

enum class IdentityStatus { IDLE, VERIFYING, VERIFIED, MISMATCH, UNAVAILABLE, INVALID_REQUEST, ERROR }

data class IdentityUiState(
    val form: IdentityForm = IdentityForm(),
    val errors: Map<IdentityField, IdentityValidationError> = emptyMap(),
    val status: IdentityStatus = IdentityStatus.IDLE,
    val verifiedIdentity: VerifiedIdentity? = null
) {
    val canContinue: Boolean get() = status == IdentityStatus.VERIFIED && verifiedIdentity != null
}

@HiltViewModel
class IdentityViewModel @Inject constructor(
    private val repository: IdentityRepository,
    private val clock: Clock
) : ViewModel() {
    private val mutableState = MutableStateFlow(IdentityUiState())
    val state = mutableState.asStateFlow()

    fun edit(field: IdentityField, value: String) {
        val current = mutableState.value
        if (current.status == IdentityStatus.VERIFYING) return
        val form = when (field) {
            IdentityField.DNI -> current.form.copy(dni = value)
            IdentityField.NAMES -> current.form.copy(names = value)
            IdentityField.LAST_NAMES -> current.form.copy(lastNames = value)
            IdentityField.BIRTH_DATE -> current.form.copy(birthDate = value)
        }
        // Editing any verified data invalidates the continuation immediately.
        mutableState.value = IdentityUiState(form, current.errors - field)
    }

    fun verify() {
        val current = mutableState.value
        if (current.status == IdentityStatus.VERIFYING) return
        val validation = IdentityValidator.validate(current.form, LocalDate.now(clock))
        val details = validation.details
        if (details == null) {
            mutableState.value = current.copy(errors = validation.errors, status = IdentityStatus.IDLE, verifiedIdentity = null)
            return
        }
        // Change state before launching to prevent two quick taps from submitting twice.
        mutableState.value = current.copy(errors = emptyMap(), status = IdentityStatus.VERIFYING, verifiedIdentity = null)
        viewModelScope.launch {
            val result = repository.verify(details)
            mutableState.value = when (result) {
                is IdentityVerificationResult.Verified -> mutableState.value.copy(status = IdentityStatus.VERIFIED, verifiedIdentity = result.identity)
                IdentityVerificationResult.Mismatch -> mutableState.value.copy(status = IdentityStatus.MISMATCH)
                IdentityVerificationResult.Unavailable -> mutableState.value.copy(status = IdentityStatus.UNAVAILABLE)
                IdentityVerificationResult.InvalidRequest -> mutableState.value.copy(status = IdentityStatus.INVALID_REQUEST)
                IdentityVerificationResult.UnexpectedError -> mutableState.value.copy(status = IdentityStatus.ERROR)
            }
        }
    }
}
