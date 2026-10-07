package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

import android.util.Patterns
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

        // Validación de formato de email
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = ChangeEmailUiState.InvalidInput(
                "Ingresa un correo válido o un celular de 9 dígitos, según el campo."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSending = true)
            // Simulación de envío
            _uiState.value = ChangeEmailUiState.CodeSent(email)
        }
    }

    fun dismissInvalidInput() {
        val current = _uiState.value
        if (current is ChangeEmailUiState.InvalidInput) {
            _uiState.value = ChangeEmailUiState.Editing()
        }
    }
}