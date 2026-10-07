package pe.edu.upc.saludya.features.configuration.presentation.linkedMinor

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

sealed interface LinkedMinorUiState {
    data object Loading : LinkedMinorUiState
    data class Success(val minor: LinkedMinor) : LinkedMinorUiState
    data class Error(val message: String) : LinkedMinorUiState
}