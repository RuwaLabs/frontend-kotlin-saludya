package pe.edu.upc.saludya.checkin.presentation.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pe.edu.upc.saludya.checkin.application.usecase.ObserveAttendanceTicketUseCase
import pe.edu.upc.saludya.checkin.infrastructure.di.CheckInModule

/**
 * ViewModel de "Ticket de atención": observa el ticket de la cita [appointmentId].
 */
class TicketViewModel(
    appointmentId: Int,
    observeTicket: ObserveAttendanceTicketUseCase,
) : ViewModel() {

    val uiState: StateFlow<TicketUiState> = observeTicket(appointmentId)
        .map { ticket -> TicketUiState(ticket = ticket) }
        .catch { emit(TicketUiState(errorMessage = it.message ?: "No se pudo cargar el ticket.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TicketUiState(isLoading = true))

    companion object {
        fun factory(appointmentId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer { TicketViewModel(appointmentId, CheckInModule.observeAttendanceTicket) }
        }
    }
}
