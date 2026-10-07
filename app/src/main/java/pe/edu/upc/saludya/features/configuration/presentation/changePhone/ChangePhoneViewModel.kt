package pe.edu.upc.saludya.features.configuration.presentation.changePhone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ChangePhoneUiState>(ChangePhoneUiState.Editing())
    val uiState: StateFlow<ChangePhoneUiState> = _uiState.asStateFlow()

    fun onPhoneChange(newValue: String) {
        val current = _uiState.value
        if (current is ChangePhoneUiState.Editing) {
            // Solo dígitos, máximo 9
            val filtered = newValue.filter { it.isDigit() }.take(9)
            _uiState.value = current.copy(newPhone = filtered)
        }
    }

    fun sendCode() {
        val current = _uiState.value
        if (current !is ChangePhoneUiState.Editing) return

        val phone = current.newPhone.trim()

        // Validación: 9 dígitos
        if (phone.length != 9) {
            _uiState.value = ChangePhoneUiState.InvalidInput(
                "Ingresa un correo válido o un celular de 9 dígitos, según el campo."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSending = true)
            // Simulación de envío
            _uiState.value = ChangePhoneUiState.CodeSent(phone)
        }
    }

    fun dismissInvalidInput() {
        val current = _uiState.value
        if (current is ChangePhoneUiState.InvalidInput) {
            _uiState.value = ChangePhoneUiState.Editing()
        }
    }
}