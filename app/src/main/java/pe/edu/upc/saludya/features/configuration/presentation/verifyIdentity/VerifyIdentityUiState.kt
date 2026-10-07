package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

sealed interface VerifyIdentityUiState {
    data class Editing(
        val minorName: String = "",
        val birthDate: String = "",
        val isSubmitting: Boolean = false,
    ) : VerifyIdentityUiState
    data object Success : VerifyIdentityUiState
    data class ValidationFailed(val message: String) : VerifyIdentityUiState
    data class Error(val message: String) : VerifyIdentityUiState
}