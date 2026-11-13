package pe.edu.upc.saludya.features.home.presentation.appointmentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.home.application.GetAppointmentByIdUseCase
import pe.edu.upc.saludya.features.patientreservations.application.usecase.CancelAppointmentUseCase
import javax.inject.Inject

@HiltViewModel
class AppointmentDetailViewModel @Inject constructor(
    private val getAppointmentById: GetAppointmentByIdUseCase,
    private val cancelAppointment: CancelAppointmentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentDetailUiState())
    val uiState: StateFlow<AppointmentDetailUiState> = _uiState.asStateFlow()

    private var appointmentId: Long = -1L

    fun setAppointmentId(id: Long) {
        if (appointmentId == id) return
        appointmentId = id
        load()
    }

    fun load() {
        if (appointmentId <= 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val appointment = getAppointmentById(appointmentId)
                _uiState.update { it.copy(appointment = appointment, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun cancel() {
        if (appointmentId <= 0 || _uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            cancelAppointment(appointmentId.toInt())
                .onSuccess { _uiState.update { state -> state.copy(isLoading = false, isCancelled = true) } }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "No se pudo cancelar la cita")
                    }
                }
        }
    }
}
