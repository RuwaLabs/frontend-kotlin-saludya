package pe.edu.upc.saludya.features.configuration.presentation.verifyCode

import androidx.annotation.StringRes

sealed interface VerifyCodeUiState {
    data class Editing(
        val code: String = "",
        val secondsRemaining: Int = 45,
        val isVerifying: Boolean = false,
        @StringRes val errorRes: Int? = null,
    ) : VerifyCodeUiState
    data object Verified : VerifyCodeUiState
    data class WrongCode(@StringRes val messageRes: Int) : VerifyCodeUiState
    data class CodeExpired(@StringRes val messageRes: Int) : VerifyCodeUiState
    data class Error(@StringRes val messageRes: Int) : VerifyCodeUiState
}