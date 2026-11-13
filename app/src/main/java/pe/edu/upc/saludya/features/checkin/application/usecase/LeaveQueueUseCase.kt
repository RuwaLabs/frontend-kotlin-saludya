package pe.edu.upc.saludya.features.checkin.application.usecase

import javax.inject.Inject

import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository

/**
 * Historia "Mi turno": el paciente abandona la cola de atención.
 * Su `queue_entries.status` pasa a ABSENT y deja de contar en las posiciones.
 */
class LeaveQueueUseCase @Inject constructor(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int) = repository.leaveQueue(appointmentId)
}