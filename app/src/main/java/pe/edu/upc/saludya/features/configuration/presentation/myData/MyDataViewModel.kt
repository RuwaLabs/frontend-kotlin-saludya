package pe.edu.upc.saludya.features.configuration.presentation.myData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.R
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

            try {
                val patient = getPatientProfileUseCase()
                _uiState.value = MyDataUiState.Success(patient)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = MyDataUiState.Error(R.string.error_generic)
            }
        }
    }
}