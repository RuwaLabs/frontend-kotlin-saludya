package pe.edu.upc.saludya.features.configuration.presentation.verifyCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
                if (current is VerifyCodeUiState.Editing && current.secondsRemaining > 0) {
                    delay(1000.milliseconds)
                    val latest = _uiState.value
                    if (latest is VerifyCodeUiState.Editing) {
                        _uiState.value = latest.copy(secondsRemaining = latest.secondsRemaining - 1)
                    }
                } else {
                    delay(1000.milliseconds)
                }
            }
        }
    }

    fun onCodeChange(newCode: String) {
        val current = _uiState.value
        if (current is VerifyCodeUiState.Editing) {
            _uiState.value = current.copy(code = newCode, error = null)
        }
    }

    fun verifyCode() {
        val current = _uiState.value
        if (current !is VerifyCodeUiState.Editing) return

        if (current.code.length < 6) {
            _uiState.value = current.copy(error = "Ingresa el código completo.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isVerifying = true, error = null)
            delay(800.milliseconds) // Simulación
            _uiState.value = VerifyCodeUiState.Verified
        }
    }

    fun resendCode() {
        val current = _uiState.value
        if (current is VerifyCodeUiState.Editing) {
            _uiState.value = current.copy(secondsRemaining = 45, code = "", error = null)
        }
    }
}