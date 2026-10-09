package pe.edu.upc.saludya.checkin.application.usecase

import pe.edu.upc.saludya.checkin.domain.entity.Reservation
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "Código QR": lista las reservas del paciente que aún admiten check-in
 * (`appointments.status` RESERVED o CONFIRMED).
 */
class GetCheckInReservationsUseCase(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(): List<Reservation> =
        repository.getReservations().filter { it.canCheckIn }
}
