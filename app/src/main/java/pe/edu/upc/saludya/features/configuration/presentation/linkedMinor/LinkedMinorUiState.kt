package pe.edu.upc.saludya.features.configuration.presentation.linkedMinor

import androidx.annotation.StringRes
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

sealed interface LinkedMinorUiState {
    data object Loading : LinkedMinorUiState
    data class Success(val minor: LinkedMinor) : LinkedMinorUiState
    data class ConfirmingUnlink(val minor: LinkedMinor) : LinkedMinorUiState
    data class Unlinked(val minorId: String) : LinkedMinorUiState
    data class Error(@StringRes val messageRes: Int) : LinkedMinorUiState
}