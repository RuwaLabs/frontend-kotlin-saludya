package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.annotation.StringRes

sealed interface VerifyIdentityUiState {
    data class Editing(
        val minorName: String = "",
        val birthDate: String = "",
        val isSubmitting: Boolean = false,
    ) : VerifyIdentityUiState
    data object Success : VerifyIdentityUiState
    data class ValidationFailed(@StringRes val messageRes: Int) : VerifyIdentityUiState
    data class Error(@StringRes val messageRes: Int) : VerifyIdentityUiState
}