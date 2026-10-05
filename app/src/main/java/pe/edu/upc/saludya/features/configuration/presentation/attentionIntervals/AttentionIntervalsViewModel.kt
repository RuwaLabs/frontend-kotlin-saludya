package pe.edu.upc.saludya.features.configuration.presentation.attentionIntervals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetAttentionIntervalUseCase
import pe.edu.upc.saludya.features.configuration.application.usecase.UpdateRuleUseCase
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import javax.inject.Inject

@HiltViewModel
class AttentionIntervalsViewModel @Inject constructor(
    private val getAttentionIntervalUseCase: GetAttentionIntervalUseCase,
    private val updateRuleUseCase: UpdateRuleUseCase,
    private val repository: ConfigurationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AttentionIntervalsUiState>(AttentionIntervalsUiState.Loading)
    val uiState: StateFlow<AttentionIntervalsUiState> = _uiState.asStateFlow()

    init {
        loadInterval()
    }

    fun loadInterval() {
        viewModelScope.launch {
            _uiState.value = AttentionIntervalsUiState.Loading
            runCatching { getAttentionIntervalUseCase() }
                .onSuccess { rule ->
                    _uiState.value = AttentionIntervalsUiState.Editing(
                        inputValue = rule.value.value,
                    )
                }
                .onFailure {
                    _uiState.value = AttentionIntervalsUiState.Error(it.message ?: "Error")
                }
        }
    }

    fun onValueChange(newValue: String) {
        val current = _uiState.value
        if (current is AttentionIntervalsUiState.Editing) {
            _uiState.value = current.copy(inputValue = newValue)
        }
    }

    fun saveInterval() {
        val current = _uiState.value
        if (current !is AttentionIntervalsUiState.Editing) return

        val value = current.inputValue.trim()

        val numeric = value.toIntOrNull()
        if (numeric == null || numeric <= 0) {
            _uiState.value = AttentionIntervalsUiState.Error("La duración debe ser mayor que cero.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSaving = true)

            val allRules = repository.getRules()
            val tolerance = allRules
                .firstOrNull { it.type == RuleType.ARRIVAL_TOLERANCE }
                ?.value?.value?.toIntOrNull()
                ?: 0

            if (numeric < tolerance) {
                _uiState.value = AttentionIntervalsUiState.Error(
                    "La duración del intervalo debe ser mayor o igual a la tolerancia de llegada ($tolerance minutos).",
                )
                return@launch
            }

            runCatching { updateRuleUseCase("attention_interval", value) }
                .onSuccess { ok ->
                    _uiState.value = if (ok) AttentionIntervalsUiState.Saved(value)
                    else AttentionIntervalsUiState.Error("No se pudo guardar el intervalo.")
                }
                .onFailure {
                    _uiState.value = AttentionIntervalsUiState.Error(it.message ?: "Error")
                }
        }
    }
}