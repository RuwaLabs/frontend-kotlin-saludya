package pe.edu.upc.saludya.features.checkin.domain.model

import java.time.LocalDateTime

/** Estados de `check_ins.status`. */
enum class CheckInStatus { VALID, EXPIRED, INVALID }

/**
 * Registro de llegada del paciente (tabla `check_ins`).
 *
 * @property id `check_ins.id`.
 * @property appointmentId `check_ins.id_appointment` (único: un check-in por cita).
 * @property qrToken `check_ins.qr_token`.
 * @property status `check_ins.status`.
 * @property checkedInAt `check_ins.checked_in_at` ("Llegada registrada").
 */
data class CheckIn(
    val id: Int,
    val appointmentId: Int,
    val qrToken: String,
    val status: CheckInStatus,
    val checkedInAt: LocalDateTime,
)
