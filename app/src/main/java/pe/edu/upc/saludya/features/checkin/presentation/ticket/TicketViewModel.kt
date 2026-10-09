package pe.edu.upc.saludya.features.checkin.presentation.ticket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pe.edu.upc.saludya.features.checkin.application.usecase.ObserveAttendanceTicketUseCase
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeTicket: ObserveAttendanceTicketUseCase,
) : ViewModel() {

    private val appointmentId: Int = checkNotNull(savedStateHandle["appointmentId"])

    val uiState: StateFlow<TicketUiState> = observeTicket(appointmentId)
        .map { ticket -> TicketUiState(ticket = ticket) }
        .catch { emit(TicketUiState(errorMessage = it.message ?: "No se pudo cargar el ticket.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TicketUiState(isLoading = true))
}
