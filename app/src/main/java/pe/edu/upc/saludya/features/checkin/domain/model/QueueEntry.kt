package pe.edu.upc.saludya.features.checkin.domain.model

import java.time.LocalDateTime

/** Estados de `queue_entries.status`. */
enum class QueueEntryStatus { WAITING, CALLED, IN_ATTENTION, ATTENDED, ABSENT }

/**
 * Turno de un paciente dentro de una cola de atención (tabla `queue_entries`).
 *
 * @property id `queue_entries.id`.
 * @property attendanceQueueId `queue_entries.id_attendance_queue`.
 * @property checkInId `queue_entries.id_check_in` (único).
 * @property position `queue_entries.position`: orden de llegada, fijo desde el check-in.
 * @property status `queue_entries.status`.
 * @property calledAt `queue_entries.called_at` ("Llamada registrada").
 * @property attendedAt `queue_entries.attended_at`.
 */
data class QueueEntry(
    val id: Int,
    val attendanceQueueId: Int,
    val checkInId: Int,
    val position: Int,
    val status: QueueEntryStatus = QueueEntryStatus.WAITING,
    val calledAt: LocalDateTime? = null,
    val attendedAt: LocalDateTime? = null,
) {
    /** Sigue ocupando un lugar en la cola (aún no pasa a consulta ni fue retirado). */
    val isActive: Boolean
        get() = status == QueueEntryStatus.WAITING || status == QueueEntryStatus.CALLED
}
