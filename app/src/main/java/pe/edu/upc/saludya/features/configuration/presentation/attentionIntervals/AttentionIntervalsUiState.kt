package pe.edu.upc.saludya.features.configuration.presentation.attentionIntervals

sealed interface AttentionIntervalsUiState {
    data object Loading : AttentionIntervalsUiState
    data class Editing(
        val inputValue: String,
        val isSaving: Boolean = false,
    ) : AttentionIntervalsUiState
    data class Saved(val intervalValue: String) : AttentionIntervalsUiState
    data class Error(val message: String) : AttentionIntervalsUiState
}