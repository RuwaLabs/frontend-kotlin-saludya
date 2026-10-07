package pe.edu.upc.saludya.features.configuration.presentation.minorHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetLinkedMinorByIdUseCase
import javax.inject.Inject

@HiltViewModel
class MinorHomeViewModel @Inject constructor(
    private val getLinkedMinorByIdUseCase: GetLinkedMinorByIdUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MinorHomeUiState>(MinorHomeUiState.Loading)
    val uiState: StateFlow<MinorHomeUiState> = _uiState.asStateFlow()

    fun loadMinor(minorId: String) {
        viewModelScope.launch {
            _uiState.value = MinorHomeUiState.Loading
            runCatching { getLinkedMinorByIdUseCase(minorId) }
                .onSuccess { minor ->
                    _uiState.value = if (minor == null) {
                        MinorHomeUiState.Error("Menor no encontrado")
                    } else {
                        MinorHomeUiState.Success(minor)
                    }
                }
                .onFailure {
                    _uiState.value = MinorHomeUiState.Error(it.message ?: "Error")
                }
        }
    }
}