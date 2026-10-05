package pe.edu.upc.saludya.features.configuration.presentation.editRule

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule

sealed interface EditRuleUiState {
    data object Loading : EditRuleUiState
    data class Success(
        val rule: ConfigurationRule,
        val inputValue: String = rule.value.value,
        val isSaving: Boolean = false,
    ) : EditRuleUiState
    data class ValidationError(
        val rule: ConfigurationRule,
        val inputValue: String,
        val message: String,
    ) : EditRuleUiState
    data class Saved(val ruleId: String) : EditRuleUiState
    data class Error(val message: String) : EditRuleUiState
}