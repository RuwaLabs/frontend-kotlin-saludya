package pe.edu.upc.saludya.features.checkin.application.usecase

import javax.inject.Inject

import pe.edu.upc.saludya.features.checkin.domain.model.CheckInException
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository

/**
 * Historia "Código QR": registra la llegada del paciente tras escanear el QR del
 * establecimiento. Crea el `check_ins` y el `queue_entries` de la cita y devuelve el ticket.
 *
 * Reglas: la cita debe existir y estar vigente (RESERVED / CONFIRMED). Es idempotente:
 * si la cita ya tiene check-in se devuelve el ticket existente.
 *
 * @throws CheckInException si la reserva no existe, no es elegible o la cola no está abierta.
 */
class RegisterArrivalUseCase @Inject constructor(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int): AttendanceTicket {
        val reservation = repository.getReservations().firstOrNull { it.id == appointmentId }
            ?: throw CheckInException.ReservationNotFound(appointmentId)
        if (!reservation.canCheckIn) throw CheckInException.ReservationNotEligible()
        return repository.registerArrival(appointmentId)
    }
}