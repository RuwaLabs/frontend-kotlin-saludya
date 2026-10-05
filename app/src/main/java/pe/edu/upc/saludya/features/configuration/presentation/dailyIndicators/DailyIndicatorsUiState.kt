package pe.edu.upc.saludya.features.configuration.presentation.dailyIndicators

import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators

sealed interface DailyIndicatorsUiState {
    data object Loading : DailyIndicatorsUiState
    data class Success(val indicators: DailyIndicators) : DailyIndicatorsUiState
    data class Error(val message: String) : DailyIndicatorsUiState
}