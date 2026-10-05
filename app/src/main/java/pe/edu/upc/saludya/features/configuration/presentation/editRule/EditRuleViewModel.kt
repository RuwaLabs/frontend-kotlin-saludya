package pe.edu.upc.saludya.features.configuration.presentation.editRule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.UpdateRuleUseCase
import pe.edu.upc.saludya.features.configuration.application.usecase.ValidateRuleCoherenceUseCase
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationResult
import javax.inject.Inject

@HiltViewModel
class EditRuleViewModel @Inject constructor(
    private val repository: ConfigurationRepository,
    private val updateRuleUseCase: UpdateRuleUseCase,
    private val validateRuleCoherenceUseCase: ValidateRuleCoherenceUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditRuleUiState>(EditRuleUiState.Loading)
    val uiState: StateFlow<EditRuleUiState> = _uiState.asStateFlow()

    fun loadRule(ruleId: String) {
        viewModelScope.launch {
            _uiState.value = EditRuleUiState.Loading
            runCatching { repository.getRuleById(ruleId) }
                .onSuccess { rule ->
                    if (rule == null) {
                        _uiState.value = EditRuleUiState.Error("Regla no encontrada")
                    } else {
                        _uiState.value = EditRuleUiState.Success(rule)
                    }
                }
                .onFailure {
                    _uiState.value = EditRuleUiState.Error(it.message ?: "Error")
                }
        }
    }

    fun onValueChange(newValue: String) {
        val current = _uiState.value
        if (current is EditRuleUiState.Success) {
            _uiState.value = current.copy(inputValue = newValue)
        }
    }

    fun saveRule() {
        val current = _uiState.value
        if (current !is EditRuleUiState.Success) return

        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)

            // 1) Validar coherencia (H05)
            val validation = validateRuleCoherenceUseCase(current.rule.id, current.inputValue)
            if (validation is ValidationResult.Invalid) {
                _uiState.value = EditRuleUiState.ValidationError(
                    rule = current.rule,
                    inputValue = current.inputValue,
                    message = validation.message,
                )
                return@launch
            }

            // 2) Actualizar
            val value = current.inputValue.trim()
            runCatching { updateRuleUseCase(current.rule.id, value) }
                .onSuccess { ok ->
                    _uiState.value = if (ok) EditRuleUiState.Saved(current.rule.id)
                    else EditRuleUiState.Error("No se pudo actualizar la regla.")
                }
                .onFailure {
                    _uiState.value = EditRuleUiState.Error(it.message ?: "Error")
                }
        }
    }

    fun dismissValidationError() {
        val current = _uiState.value
        if (current is EditRuleUiState.ValidationError) {
            _uiState.value = EditRuleUiState.Success(
                rule = current.rule,
                inputValue = current.inputValue,
            )
        }
    }
}