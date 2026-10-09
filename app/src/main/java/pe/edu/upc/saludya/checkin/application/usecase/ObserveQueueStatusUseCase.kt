package pe.edu.upc.saludya.checkin.application.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket
import pe.edu.upc.saludya.checkin.domain.entity.QueuePosition
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "Mi turno": observa la posición del paciente en la cola de atención junto con
 * su ticket y con la regla del establecimiento sobre si la cola es visible.
 */
class ObserveQueueStatusUseCase(
    private val repository: CheckInRepository,
) {
    operator fun invoke(appointmentId: Int): Flow<QueueStatus> = combine(
        repository.observeTicket(appointmentId),
        repository.observeQueuePosition(appointmentId),
        flow { emit(repository.getConfiguration()) },
    ) { ticket, position, configuration ->
        QueueStatus(
            ticket = ticket,
            position = position,
            isQueueVisible = configuration.attendanceQueueVisible,
        )
    }
}

/**
 * Resultado del caso de uso "consultar mi posición en la cola".
 *
 * @property ticket ticket del paciente; `null` si no hay check-in.
 * @property position posición actual; `null` si el turno ya no está activo.
 * @property isQueueVisible `hospital_configurations.attendance_queue_visible`.
 */
data class QueueStatus(
    val ticket: AttendanceTicket?,
    val position: QueuePosition?,
    val isQueueVisible: Boolean,
)
