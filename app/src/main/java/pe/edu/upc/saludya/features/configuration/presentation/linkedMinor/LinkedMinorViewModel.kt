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

    private var currentMinorId: String = ""

    fun loadMinor(minorId: String) {
        currentMinorId = minorId
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

    /**
     * El usuario tocó "Dejar vinculación".
     * Muestra el modal de confirmación.
     */
    fun requestUnlink() {
        val current = _uiState.value
        if (current is LinkedMinorUiState.Success) {
            _uiState.value = LinkedMinorUiState.ConfirmingUnlink(current.minor)
        }
    }

    /**
     * El usuario confirmó "Dejar vinculación" en el modal.
     * Cambia el estado a Unlinked → NavHost navega hacia atrás.
     */
    fun confirmUnlink() {
        val current = _uiState.value
        if (current is LinkedMinorUiState.ConfirmingUnlink) {
            // Simulación de "dejar vinculación" en el backend
            _uiState.value = LinkedMinorUiState.Unlinked(current.minor.id)
        }
    }

    /**
     * El usuario tocó "Conservar vínculo".
     * Cierra el modal y vuelve al estado Success.
     */
    fun cancelUnlink() {
        val current = _uiState.value
        if (current is LinkedMinorUiState.ConfirmingUnlink) {
            _uiState.value = LinkedMinorUiState.Success(current.minor)
        }
    }
}