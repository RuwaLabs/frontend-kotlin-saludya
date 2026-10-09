package pe.edu.upc.saludya.features.checkin.domain.model

import java.time.LocalDate

/** Estados de `attendance_queues.status`. */
enum class AttendanceQueueStatus { OPEN, CLOSED, PAUSED }

/**
 * Cola de atención presencial de un bloque horario (tabla `attendance_queues`).
 *
 * @property id `attendance_queues.id`.
 * @property timeSlotId `attendance_queues.id_time_slot`.
 * @property date `attendance_queues.date`.
 * @property status `attendance_queues.status`.
 */
data class AttendanceQueue(
    val id: Int,
    val timeSlotId: Int,
    val date: LocalDate,
    val status: AttendanceQueueStatus = AttendanceQueueStatus.OPEN,
)
