package pe.edu.upc.saludya.features.configuration.presentation.additionalRules

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule

sealed interface AdditionalRulesUiState {
    data object Loading : AdditionalRulesUiState
    data class Success(val rules: List<ConfigurationRule>) : AdditionalRulesUiState
    data class Error(val message: String) : AdditionalRulesUiState
}