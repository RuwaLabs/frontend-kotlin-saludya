package pe.edu.upc.saludya.features.configuration.presentation.dailyIndicators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetDailyIndicatorsUseCase
import javax.inject.Inject

@HiltViewModel
class DailyIndicatorsViewModel @Inject constructor(
    private val getDailyIndicatorsUseCase: GetDailyIndicatorsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DailyIndicatorsUiState>(DailyIndicatorsUiState.Loading)
    val uiState: StateFlow<DailyIndicatorsUiState> = _uiState.asStateFlow()

    init {
        loadIndicators()
    }

    fun loadIndicators() {
        viewModelScope.launch {
            _uiState.value = DailyIndicatorsUiState.Loading
            runCatching { getDailyIndicatorsUseCase() }
                .onSuccess { _uiState.value = DailyIndicatorsUiState.Success(it) }
                .onFailure { _uiState.value = DailyIndicatorsUiState.Error(it.message ?: "Error") }
        }
    }
}