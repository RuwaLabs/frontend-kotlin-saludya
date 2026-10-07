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
            _uiState.value = current.copy(newPhone = newValue)
        }
    }

    fun sendCode() {
        val current = _uiState.value
        if (current !is ChangePhoneUiState.Editing) return

        val phone = current.newPhone.trim()
        if (phone.isEmpty()) {
            _uiState.value = ChangePhoneUiState.Error("Ingresa un número de celular válido.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSending = true)
            // Simulación de envío de código (mock)
            _uiState.value = ChangePhoneUiState.CodeSent(phone)
        }
    }
}