package pe.edu.upc.saludya.features.configuration.presentation.configuration

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule

sealed interface ConfigurationUiState {
    data object Loading : ConfigurationUiState
    data class Success(val rules: List<ConfigurationRule>) : ConfigurationUiState
    data class Error(val message: String) : ConfigurationUiState
}