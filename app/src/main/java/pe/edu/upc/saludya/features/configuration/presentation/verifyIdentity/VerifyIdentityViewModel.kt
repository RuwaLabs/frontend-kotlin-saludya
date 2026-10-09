package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.R
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

        // Validación básica de campos no vacíos
        if (name.isEmpty()) {
            _uiState.value = VerifyIdentityUiState.ValidationFailed(
                R.string.error_validation_failed
            )
            return
        }
        if (date.isEmpty()) {
            _uiState.value = VerifyIdentityUiState.ValidationFailed(
                R.string.error_validation_failed
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSubmitting = true)
            // Simulación de validación contra registros de admisión
            // Datos esperados: "Mateo" + "12/04/2018"
            val validName = name.lowercase().startsWith("mateo")
            val validDate = date == "12/04/2018"

            if (validName && validDate) {
                _uiState.value = VerifyIdentityUiState.Success
            } else {
                _uiState.value = VerifyIdentityUiState.ValidationFailed(
                    R.string.error_validation_failed
                )
            }
        }
    }

    fun dismissValidationFailed() {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.ValidationFailed) {
            _uiState.value = VerifyIdentityUiState.Editing(
                minorName = "",
                birthDate = "",
            )
        }
    }
}