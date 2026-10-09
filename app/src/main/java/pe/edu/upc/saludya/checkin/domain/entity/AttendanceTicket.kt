package pe.edu.upc.saludya.checkin.domain.entity

/**
 * Ticket de atención: modelo de lectura que reúne la reserva, su check-in y su turno.
 *
 * @property code código visible del turno (ej. "MG-003"). **No es una columna**: se deriva
 *   con [buildTicketCode] a partir de `specialties.name` y `queue_entries.position`.
 */
data class AttendanceTicket(
    val code: String,
    val reservation: Reservation,
    val checkIn: CheckIn,
    val queueEntry: QueueEntry,
) {
    val isCalled: Boolean
        get() = queueEntry.status == QueueEntryStatus.CALLED
}

/**
 * Código de turno: iniciales de la especialidad + posición de llegada a 3 dígitos.
 *
 * "Medicina general", 3 → "MG-003" · "Pediatría", 12 → "PE-012".
 */
fun buildTicketCode(specialty: String, position: Int): String {
    val words = specialty.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    val prefix = when {
        words.isEmpty() -> "T"
        words.size == 1 -> words.first().take(2)
        else -> words.joinToString("") { it.take(1) }
    }.uppercase()
    return "$prefix-${position.toString().padStart(3, '0')}"
}
