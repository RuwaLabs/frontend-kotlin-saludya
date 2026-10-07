package pe.edu.upc.saludya.features.configuration.presentation.linkMinor

sealed interface LinkMinorUiState {
    data class Editing(
        val documentType: String = "DNI",
        val documentNumber: String = "",
        val isVerifying: Boolean = false,
    ) : LinkMinorUiState
    data class Verified(val documentType: String, val documentNumber: String) : LinkMinorUiState
    data class Error(val message: String) : LinkMinorUiState
}