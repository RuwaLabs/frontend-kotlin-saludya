package pe.edu.upc.saludya.checkin.application.usecase

import kotlinx.coroutines.flow.Flow
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "Ticket de atención": observa el ticket de una cita.
 * Emite `null` mientras la cita no tenga llegada registrada.
 */
class ObserveAttendanceTicketUseCase(
    private val repository: CheckInRepository,
) {
    operator fun invoke(appointmentId: Int): Flow<AttendanceTicket?> =
        repository.observeTicket(appointmentId)
}
