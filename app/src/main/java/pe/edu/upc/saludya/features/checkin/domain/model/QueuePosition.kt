package pe.edu.upc.saludya.features.checkin.domain.model

import java.time.LocalTime

/**
 * Posición actual del paciente en su cola de atención ("3 de 8").
 *
 * Se calcula sobre los `queue_entries` activos (WAITING / CALLED) de la misma cola,
 * ordenados por `position`.
 *
 * @property current lugar actual del paciente (1 = el siguiente).
 * @property total turnos activos en la cola.
 * @property queueStatus `attendance_queues.status`.
 * @property updatedAt hora de la última lectura.
 */
data class QueuePosition(
    val current: Int,
    val total: Int,
    val queueStatus: AttendanceQueueStatus,
    val updatedAt: LocalTime,
) {
    val peopleAhead: Int
        get() = (current - 1).coerceAtLeast(0)
}
