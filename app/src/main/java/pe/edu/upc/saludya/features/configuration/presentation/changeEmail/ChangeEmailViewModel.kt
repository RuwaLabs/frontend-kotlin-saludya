package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ChangeEmailUiState>(ChangeEmailUiState.Editing())
    val uiState: StateFlow<ChangeEmailUiState> = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        val current = _uiState.value
        if (current is ChangeEmailUiState.Editing) {
            _uiState.value = current.copy(newEmail = newValue)
        }
    }

    fun sendCode() {
        val current = _uiState.value
        if (current !is ChangeEmailUiState.Editing) return

        val email = current.newEmail.trim()
        if (email.isEmpty() || !email.contains("@")) {
            _uiState.value = ChangeEmailUiState.Error("Ingresa un correo válido.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSending = true)
            // Simulación de envío de código (mock)
            _uiState.value = ChangeEmailUiState.CodeSent(email)
        }
    }
}