package pe.edu.upc.saludya.features.configuration.presentation.specialtyDemand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetDemandBySpecialtyUseCase
import javax.inject.Inject

@HiltViewModel
class SpecialtyDemandViewModel @Inject constructor(
    private val getDemandBySpecialtyUseCase: GetDemandBySpecialtyUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SpecialtyDemandUiState>(SpecialtyDemandUiState.Loading)
    val uiState: StateFlow<SpecialtyDemandUiState> = _uiState.asStateFlow()

    init {
        loadDemand()
    }

    fun loadDemand() {
        viewModelScope.launch {
            _uiState.value = SpecialtyDemandUiState.Loading
            runCatching { getDemandBySpecialtyUseCase() }
                .onSuccess { _uiState.value = SpecialtyDemandUiState.Success(it) }
                .onFailure { _uiState.value = SpecialtyDemandUiState.Error(it.message ?: "Error") }
        }
    }
}