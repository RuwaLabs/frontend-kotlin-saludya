package pe.edu.upc.saludya.features.configuration.presentation.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.configuration.application.usecase.GetPatientProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ConfigurationViewModel @Inject constructor(
    private val getPatientProfileUseCase: GetPatientProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConfigurationUiState>(ConfigurationUiState.Loading)
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ConfigurationUiState.Loading
            runCatching { getPatientProfileUseCase() }
                .onSuccess { _uiState.value = ConfigurationUiState.Success(it) }
                .onFailure { _uiState.value = ConfigurationUiState.Error(R.string.error_generic) }
        }
    }
}