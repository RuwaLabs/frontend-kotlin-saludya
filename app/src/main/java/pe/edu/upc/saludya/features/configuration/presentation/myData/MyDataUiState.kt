package pe.edu.upc.saludya.features.configuration.presentation.myData

import pe.edu.upc.saludya.features.configuration.domain.entity.Patient

sealed interface MyDataUiState {
    data object Loading : MyDataUiState
    data class Success(val patient: Patient) : MyDataUiState
    data class Error(val message: String) : MyDataUiState
}