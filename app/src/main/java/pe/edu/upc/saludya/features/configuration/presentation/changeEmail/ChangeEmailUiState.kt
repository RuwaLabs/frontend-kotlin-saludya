package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

import androidx.annotation.StringRes

sealed interface ChangeEmailUiState {
    data class Editing(
        val newEmail: String = "",
        val isSending: Boolean = false,
    ) : ChangeEmailUiState
    data class CodeSent(val newEmail: String) : ChangeEmailUiState
    data class InvalidInput(@StringRes val messageRes: Int) : ChangeEmailUiState
    data class Error(@StringRes val messageRes: Int) : ChangeEmailUiState
}