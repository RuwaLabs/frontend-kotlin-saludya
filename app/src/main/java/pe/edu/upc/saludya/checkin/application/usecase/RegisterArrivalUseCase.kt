package pe.edu.upc.saludya.checkin.application.usecase

import pe.edu.upc.saludya.checkin.domain.entity.CheckInException
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "Código QR": registra la llegada del paciente tras escanear el QR del
 * establecimiento. Crea el `check_ins` y el `queue_entries` de la cita y devuelve el ticket.
 *
 * Reglas: la cita debe existir y estar vigente (RESERVED / CONFIRMED). Es idempotente:
 * si la cita ya tiene check-in se devuelve el ticket existente.
 *
 * @throws CheckInException si la reserva no existe, no es elegible o la cola no está abierta.
 */
class RegisterArrivalUseCase(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int): AttendanceTicket {
        val reservation = repository.getReservations().firstOrNull { it.id == appointmentId }
            ?: throw CheckInException.ReservationNotFound(appointmentId)
        if (!reservation.canCheckIn) throw CheckInException.ReservationNotEligible()
        return repository.registerArrival(appointmentId)
    }
}
