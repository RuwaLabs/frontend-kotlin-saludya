package pe.edu.upc.saludya.checkin.application.usecase

import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "Mi turno": el paciente abandona la cola de atención.
 * Su `queue_entries.status` pasa a ABSENT y deja de contar en las posiciones.
 */
class LeaveQueueUseCase(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int) = repository.leaveQueue(appointmentId)
}
