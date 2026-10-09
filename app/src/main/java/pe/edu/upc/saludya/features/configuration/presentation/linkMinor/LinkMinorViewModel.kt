package pe.edu.upc.saludya.features.configuration.presentation.linkMinor

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
class LinkMinorViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<LinkMinorUiState>(LinkMinorUiState.Editing())
    val uiState: StateFlow<LinkMinorUiState> = _uiState.asStateFlow()

    fun onDocumentTypeChange(newValue: String) {
        val current = _uiState.value
        if (current is LinkMinorUiState.Editing) {
            _uiState.value = current.copy(documentType = newValue)
        }
    }

    fun onDocumentNumberChange(newValue: String) {
        val current = _uiState.value
        if (current is LinkMinorUiState.Editing) {
            _uiState.value = current.copy(documentNumber = newValue)
        }
    }

    fun verifyDocument() {
        val current = _uiState.value
        if (current !is LinkMinorUiState.Editing) return

        val number = current.documentNumber.trim()
        if (number.isEmpty()) {
            _uiState.value = LinkMinorUiState.Error(R.string.error_empty_document)
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isVerifying = true)
            // Simulación
            _uiState.value = LinkMinorUiState.Verified(
                documentType = current.documentType,
                documentNumber = number,
            )
        }
    }
}