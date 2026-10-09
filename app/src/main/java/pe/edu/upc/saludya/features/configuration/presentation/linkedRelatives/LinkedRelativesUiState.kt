package pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives

import androidx.annotation.StringRes
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

sealed interface LinkedRelativesUiState {
    data object Loading : LinkedRelativesUiState
    data class Success(
        val minors: List<LinkedMinor>,
        val justSynced: Boolean = false,
    ) : LinkedRelativesUiState
    data class Error(@StringRes val messageRes: Int) : LinkedRelativesUiState
}