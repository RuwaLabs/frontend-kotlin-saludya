package pe.edu.upc.saludya.features.configuration.presentation.validation

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val message: String) : ValidationResult
}