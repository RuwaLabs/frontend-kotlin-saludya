package pe.edu.upc.saludya.features.configuration.presentation.minorHome

import androidx.annotation.StringRes
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

sealed interface MinorHomeUiState {
    data object Loading : MinorHomeUiState
    data class Success(val minor: LinkedMinor) : MinorHomeUiState
    data class Error(@StringRes val messageRes: Int) : MinorHomeUiState
}