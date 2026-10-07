package pe.edu.upc.saludya.reassignment.domain

import java.time.Instant

data class ReassignmentOffer(
    val id: Long,
    val appointmentId: Long,
    val originalAppointmentId: Long,
    val freedTimeSlotId: Long,
    val candidateTimeSlotId: Long,
    val status: String,
    val offeredAt: Instant,
    val respondedAt: Instant?,
    val expiresAt: Instant
)
