package pe.edu.upc.saludya.features.configuration.presentation.verifyCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.R
import kotlin.time.Duration.Companion.milliseconds
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<VerifyCodeUiState>(VerifyCodeUiState.Editing())
    val uiState: StateFlow<VerifyCodeUiState> = _uiState.asStateFlow()

    init {
        startCountdown()
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (true) {
                val current = _uiState.value
                when {
                    current is VerifyCodeUiState.Editing && current.secondsRemaining > 0 -> {
                        delay(1000.milliseconds)
                        val latest = _uiState.value
                        if (latest is VerifyCodeUiState.Editing) {
                            val nextSecond = latest.secondsRemaining - 1
                            if (nextSecond <= 0) {
                                // El contador llegó a cero → código vencido
                                _uiState.value = VerifyCodeUiState.CodeExpired(
                                    R.string.error_code_expired_message
                                )
                            } else {
                                _uiState.value = latest.copy(secondsRemaining = nextSecond)
                            }
                        }
                    }
                    else -> {
                        delay(1000.milliseconds)
                    }
                }
            }
        }
    }

    fun onCodeChange(newCode: String) {
        val current = _uiState.value
        if (current is VerifyCodeUiState.Editing) {
            _uiState.value = current.copy(code = newCode, errorRes = null)
        }
    }

    fun verifyCode() {
        val current = _uiState.value
        if (current !is VerifyCodeUiState.Editing) return

        if (current.code.length < 6) {
            _uiState.value = current.copy(errorRes = R.string.error_incomplete_code)
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isVerifying = true, errorRes = null)
            delay(800.milliseconds)

            // Código correcto simulado: "241860"
            if (current.code == "241860") {
                _uiState.value = VerifyCodeUiState.Verified
            } else {
                _uiState.value = VerifyCodeUiState.WrongCode(
                    R.string.error_wrong_code_message
                )
            }
        }
    }

    fun dismissWrongCode() {
        val current = _uiState.value
        if (current is VerifyCodeUiState.WrongCode) {
            _uiState.value = VerifyCodeUiState.Editing()
        }
    }

    fun dismissCodeExpired() {
        val current = _uiState.value
        if (current is VerifyCodeUiState.CodeExpired) {
            _uiState.value = VerifyCodeUiState.Editing(
                code = "",
                secondsRemaining = 45,
            )
        }
    }

    fun resendCode() {
        val current = _uiState.value
        if (current is VerifyCodeUiState.Editing) {
            _uiState.value = current.copy(
                secondsRemaining = 45,
                code = "",
                errorRes = null,
            )
        }
    }
}