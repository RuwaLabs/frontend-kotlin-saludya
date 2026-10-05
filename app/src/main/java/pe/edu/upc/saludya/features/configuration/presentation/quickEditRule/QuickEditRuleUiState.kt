package pe.edu.upc.saludya.features.configuration.presentation.quickEditRule

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule

sealed interface QuickEditRuleUiState {
    data object Loading : QuickEditRuleUiState
    data class Editing(
        val rule: ConfigurationRule,
        val inputValue: String = "",
        val isSaving: Boolean = false,
        val validationError: String? = null,
    ) : QuickEditRuleUiState
    data class Saved(val ruleId: String) : QuickEditRuleUiState
    data class Error(val message: String) : QuickEditRuleUiState
}