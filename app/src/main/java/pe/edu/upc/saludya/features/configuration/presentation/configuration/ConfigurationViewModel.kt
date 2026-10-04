package pe.edu.upc.saludya.features.configuration.presentation.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetConfigurationRulesUseCase
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val getConfigurationRulesUseCase: GetConfigurationRulesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfigurationUiState>(ConfigurationUiState.Loading)
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()

    init {
        loadRules()
    }

    fun loadRules() {
        viewModelScope.launch {
            _uiState.value = ConfigurationUiState.Loading
            runCatching { getConfigurationRulesUseCase() }
                .onSuccess { _uiState.value = ConfigurationUiState.Success(it) }
                .onFailure { _uiState.value = ConfigurationUiState.Error(it.message ?: "Error") }
        }
    }
}