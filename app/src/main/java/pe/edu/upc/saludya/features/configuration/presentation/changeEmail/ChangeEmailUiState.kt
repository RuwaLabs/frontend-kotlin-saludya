package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

sealed interface ChangeEmailUiState {
    data class Editing(
        val newEmail: String = "",
        val isSending: Boolean = false,
    ) : ChangeEmailUiState
    data class CodeSent(val newEmail: String) : ChangeEmailUiState
    data class Error(val message: String) : ChangeEmailUiState
}