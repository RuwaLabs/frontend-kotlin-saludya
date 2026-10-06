package pe.edu.upc.saludya.features.configuration.presentation.myData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.GetPatientProfileUseCase
import javax.inject.Inject

@HiltViewModel
class MyDataViewModel @Inject constructor(
    private val getPatientProfileUseCase: GetPatientProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyDataUiState>(MyDataUiState.Loading)
    val uiState: StateFlow<MyDataUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = MyDataUiState.Loading
            runCatching { getPatientProfileUseCase() }
                .onSuccess { _uiState.value = MyDataUiState.Success(it) }
                .onFailure { _uiState.value = MyDataUiState.Error(it.message ?: "Error") }
        }
    }
}