package pe.edu.upc.saludya.home.presentation.pendingappointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.home.application.GetUpcomingAppointmentsUseCase
import javax.inject.Inject

@HiltViewModel
class PendingAppointmentsViewModel @Inject constructor(
    private val getUpcomingAppointments: GetUpcomingAppointmentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PendingAppointmentsUiState())
    val uiState: StateFlow<PendingAppointmentsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val appointments = getUpcomingAppointments()
                _uiState.update { it.copy(appointments = appointments, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
