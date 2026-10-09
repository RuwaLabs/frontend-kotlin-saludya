package pe.edu.upc.saludya.features.configuration.presentation.myData

import androidx.annotation.StringRes
import pe.edu.upc.saludya.features.configuration.domain.entity.Patient

sealed interface MyDataUiState {
    data object Loading : MyDataUiState
    data class Success(val patient: Patient) : MyDataUiState
    data class Error(@StringRes val messageRes: Int) : MyDataUiState
}