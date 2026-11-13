package pe.edu.upc.saludya.features.reassignment.infrastructure.remote

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
