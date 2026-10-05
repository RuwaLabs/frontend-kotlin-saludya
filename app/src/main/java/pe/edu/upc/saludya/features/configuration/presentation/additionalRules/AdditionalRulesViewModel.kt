package pe.edu.upc.saludya.features.configuration.presentation.additionalRules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetAdditionalRulesUseCase
import javax.inject.Inject

@HiltViewModel
class AdditionalRulesViewModel @Inject constructor(
    private val getAdditionalRulesUseCase: GetAdditionalRulesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdditionalRulesUiState>(AdditionalRulesUiState.Loading)
    val uiState: StateFlow<AdditionalRulesUiState> = _uiState.asStateFlow()

    init {
        loadRules()
    }

    fun loadRules() {
        viewModelScope.launch {
            _uiState.value = AdditionalRulesUiState.Loading
            runCatching { getAdditionalRulesUseCase() }
                .onSuccess { _uiState.value = AdditionalRulesUiState.Success(it) }
                .onFailure { _uiState.value = AdditionalRulesUiState.Error(it.message ?: "Error") }
        }
    }
}