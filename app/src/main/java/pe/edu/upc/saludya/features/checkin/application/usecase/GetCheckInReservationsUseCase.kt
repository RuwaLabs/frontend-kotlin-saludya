package pe.edu.upc.saludya.features.checkin.application.usecase

import javax.inject.Inject

import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository

/**
 * Historia "Código QR": lista las reservas del paciente que aún admiten check-in
 * (`appointments.status` RESERVED o CONFIRMED).
 */
class GetCheckInReservationsUseCase @Inject constructor(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(): List<Reservation> =
        repository.getReservations().filter { it.canCheckIn }
}