package pe.edu.upc.saludya.features.configuration.presentation.changePhone

sealed interface ChangePhoneUiState {
    data class Editing(
        val newPhone: String = "",
        val isSending: Boolean = false,
    ) : ChangePhoneUiState
    data class CodeSent(val newPhone: String) : ChangePhoneUiState
    data class Error(val message: String) : ChangePhoneUiState
}