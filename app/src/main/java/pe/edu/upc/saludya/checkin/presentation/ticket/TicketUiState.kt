package pe.edu.upc.saludya.checkin.presentation.ticket

import androidx.compose.runtime.Immutable
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket

/**
 * Estado inmutable de la pantalla "Ticket de atención".
 *
 * @property ticket ticket de la cita; `null` si aún no hay check-in.
 */
@Immutable
data class TicketUiState(
    val isLoading: Boolean = false,
    val ticket: AttendanceTicket? = null,
    val errorMessage: String? = null,
)
