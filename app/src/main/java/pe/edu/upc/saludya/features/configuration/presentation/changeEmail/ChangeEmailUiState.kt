package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

import androidx.annotation.StringRes

sealed interface ChangeEmailUiState {
    data class Editing(
        val newEmail: String = "",
        val isSending: Boolean = false,
    ) : ChangeEmailUiState
    data object Saved : ChangeEmailUiState
    data class InvalidInput(@StringRes val messageRes: Int) : ChangeEmailUiState
    data class Error(@StringRes val messageRes: Int) : ChangeEmailUiState
}
