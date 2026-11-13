package pe.edu.upc.saludya.features.checkin.application.usecase

import javax.inject.Inject

import kotlinx.coroutines.flow.Flow
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository

/**
 * Historia "Ticket de atención": observa el ticket de una cita.
 * Emite `null` mientras la cita no tenga llegada registrada.
 */
class ObserveAttendanceTicketUseCase @Inject constructor(
    private val repository: CheckInRepository,
) {
    operator fun invoke(appointmentId: Int): Flow<AttendanceTicket?> =
        repository.observeTicket(appointmentId)
}