package pe.edu.upc.saludya.features.checkin.domain.model

/**
 * Código QR personal de una reserva.
 *
 * @property token valor que se guarda en `check_ins.qr_token` al registrar la llegada
 *   (ej. "SY-261006-0142": prefijo + fecha `yyMMdd` + `appointments.id` a 4 dígitos).
 * @property appointmentId `appointments.id` al que pertenece.
 * @property isSample `true` mientras el token no lo emita el backend.
 */
data class QrCodeInfo(
    val token: String,
    val appointmentId: Int,
    val isSample: Boolean = true,
)
