package pe.edu.upc.saludya.reassignment.infrastructure.remote

/**
 * Payload returned by GET /reassignment-offers/pending and by the accept/reject endpoints.
 *
 * Dates are kept as String because Gson does not know how to deserialize java.time.Instant
 * out of the box (the backend sends ISO-8601 strings). They are parsed to Instant in the mapper.
 */
data class ReassignmentOfferDto(
    val id: Long,
    val appointmentId: Long,
    val originalAppointmentId: Long,
    val freedTimeSlotId: Long,
    val candidateTimeSlotId: Long,
    val status: String,
    val offeredAt: String,
    val respondedAt: String?,
    val expiresAt: String
)
