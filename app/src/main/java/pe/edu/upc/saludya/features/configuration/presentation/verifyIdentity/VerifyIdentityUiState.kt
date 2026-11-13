package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.annotation.StringRes

sealed interface VerifyIdentityUiState {
    data class Editing(
        val minorName: String = "",
        val minorLastname: String = "",
        val birthDate: String = "",
        val isSubmitting: Boolean = false,
    ) : VerifyIdentityUiState
    data class Success(val linkId: String) : VerifyIdentityUiState
    data class ValidationFailed(@StringRes val messageRes: Int) : VerifyIdentityUiState
    data class Failure(val message: String) : VerifyIdentityUiState
    data class Error(@StringRes val messageRes: Int) : VerifyIdentityUiState
}
