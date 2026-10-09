package pe.edu.upc.saludya.checkin.domain.entity

/**
 * Errores de negocio del contexto "Arrival & QR Check-in".
 * El mensaje está pensado para mostrarse al paciente.
 */
sealed class CheckInException(message: String) : Exception(message) {

    /** No existe la cita (`appointments.id`) indicada. */
    class ReservationNotFound(appointmentId: Int) :
        CheckInException("No se encontró la reserva $appointmentId.")

    /** La cita no está en RESERVED / CONFIRMED, por lo que no admite check-in. */
    class ReservationNotEligible :
        CheckInException("Esta reserva ya no admite el registro de llegada.")

    /** La cola del bloque horario (`attendance_queues.status`) no está OPEN. */
    class QueueNotOpen :
        CheckInException("La cola de atención no está abierta en este momento.")
}
