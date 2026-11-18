package pe.edu.upc.saludya.features.checkin.domain.model

/**
 * Código QR personal de una reserva.
 *
 * @property token JWT firmado generado por el backend.
 * @property appointmentId ID de la cita a la que pertenece.
 * @property isSample indica si el QR es únicamente ilustrativo.
 */
data class QrCodeInfo(
    val token: String,
    val appointmentId: Int,
    val isSample: Boolean = false,
)