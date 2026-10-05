package pe.edu.upc.saludya.features.configuration.presentation.specialtyDemand

import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemand

sealed interface SpecialtyDemandUiState {
    data object Loading : SpecialtyDemandUiState
    data class Success(val demand: SpecialtyDemand) : SpecialtyDemandUiState
    data class Error(val message: String) : SpecialtyDemandUiState
}