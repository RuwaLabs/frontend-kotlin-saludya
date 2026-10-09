package pe.edu.upc.saludya.features.configuration.presentation.changePhone

import androidx.annotation.StringRes

sealed interface ChangePhoneUiState {
    data class Editing(
        val newPhone: String = "",
        val isSending: Boolean = false,
    ) : ChangePhoneUiState
    data class CodeSent(val newPhone: String) : ChangePhoneUiState
    data class InvalidInput(@StringRes val messageRes: Int) : ChangePhoneUiState
    data class Error(@StringRes val messageRes: Int) : ChangePhoneUiState
}