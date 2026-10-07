package pe.edu.upc.saludya.features.configuration.presentation.verifyCode

sealed interface VerifyCodeUiState {
    data class Editing(
        val code: String = "",
        val secondsRemaining: Int = 45,
        val isVerifying: Boolean = false,
        val error: String? = null,
    ) : VerifyCodeUiState
    data object Verified : VerifyCodeUiState
    data class Error(val message: String) : VerifyCodeUiState
}