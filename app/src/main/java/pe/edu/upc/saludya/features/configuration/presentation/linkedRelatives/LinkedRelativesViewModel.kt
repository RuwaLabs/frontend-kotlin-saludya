package pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetLinkedMinorsUseCase
import javax.inject.Inject

@HiltViewModel
class LinkedRelativesViewModel @Inject constructor(
    private val getLinkedMinorsUseCase: GetLinkedMinorsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LinkedRelativesUiState>(LinkedRelativesUiState.Loading)
    val uiState: StateFlow<LinkedRelativesUiState> = _uiState.asStateFlow()

    init {
        loadLinkedMinors()
    }

    fun loadLinkedMinors() {
        viewModelScope.launch {
            _uiState.value = LinkedRelativesUiState.Loading
            runCatching { getLinkedMinorsUseCase() }
                .onSuccess { _uiState.value = LinkedRelativesUiState.Success(it) }
                .onFailure { _uiState.value = LinkedRelativesUiState.Error(it.message ?: "Error") }
        }
    }
}