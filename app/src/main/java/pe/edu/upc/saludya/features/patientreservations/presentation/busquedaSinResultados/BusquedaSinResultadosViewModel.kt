package pe.edu.upc.saludya.features.patientreservations.presentation.busquedaSinResultados

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BusquedaSinResultadosViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BusquedaSinResultadosUiState())
    val uiState: StateFlow<BusquedaSinResultadosUiState> = _uiState.asStateFlow()

    fun start(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}
