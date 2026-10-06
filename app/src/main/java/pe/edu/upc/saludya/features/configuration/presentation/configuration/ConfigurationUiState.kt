package pe.edu.upc.saludya.features.configuration.presentation.configuration

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient

sealed interface ConfigurationUiState {
    data object Loading : ConfigurationUiState
    data class Success(val patient: Patient) : ConfigurationUiState
    data class Error(val message: String) : ConfigurationUiState
}