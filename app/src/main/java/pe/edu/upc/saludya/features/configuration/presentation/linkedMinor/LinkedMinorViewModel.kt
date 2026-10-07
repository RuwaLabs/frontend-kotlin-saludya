package pe.edu.upc.saludya.features.configuration.presentation.linkedMinor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetLinkedMinorByIdUseCase
import javax.inject.Inject

@HiltViewModel
class LinkedMinorViewModel @Inject constructor(
    private val getLinkedMinorByIdUseCase: GetLinkedMinorByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LinkedMinorUiState>(LinkedMinorUiState.Loading)
    val uiState: StateFlow<LinkedMinorUiState> = _uiState.asStateFlow()

    fun loadMinor(minorId: String) {
        viewModelScope.launch {
            _uiState.value = LinkedMinorUiState.Loading
            runCatching { getLinkedMinorByIdUseCase(minorId) }
                .onSuccess { minor ->
                    _uiState.value = if (minor == null) {
                        LinkedMinorUiState.Error("Menor no encontrado")
                    } else {
                        LinkedMinorUiState.Success(minor)
                    }
                }
                .onFailure {
                    _uiState.value = LinkedMinorUiState.Error(it.message ?: "Error")
                }
        }
    }
}