package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyIdentityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<VerifyIdentityUiState>(VerifyIdentityUiState.Editing())
    val uiState: StateFlow<VerifyIdentityUiState> = _uiState.asStateFlow()

    fun onMinorNameChange(newValue: String) {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.Editing) {
            _uiState.value = current.copy(minorName = newValue)
        }
    }

    fun onBirthDateChange(newValue: String) {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.Editing) {
            _uiState.value = current.copy(birthDate = newValue)
        }
    }

    fun submit() {
        val current = _uiState.value
        if (current !is VerifyIdentityUiState.Editing) return

        val name = current.minorName.trim()
        val date = current.birthDate.trim()

        if (name.isEmpty()) {
            _uiState.value = VerifyIdentityUiState.Error("Ingresa los nombres del menor.")
            return
        }
        if (date.isEmpty()) {
            _uiState.value = VerifyIdentityUiState.Error("Ingresa la fecha de nacimiento.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSubmitting = true)
            // Simulación
            _uiState.value = VerifyIdentityUiState.Success
        }
    }
}