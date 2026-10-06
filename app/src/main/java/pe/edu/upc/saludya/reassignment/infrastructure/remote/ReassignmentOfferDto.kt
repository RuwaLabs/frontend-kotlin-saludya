package pe.edu.upc.saludya.reassignment.infrastructure.remote

import java.time.Instant

data class ReassignmentOfferDto(
    val id: Int,
    val appointmentId: Long,
    val originalAppointmentId: Long,
    val freedTimeSlotId: Long,
    val status: String,
    val offeredAt: Instant,
    val respondedAt: Instant,
    val expiresAt: Instant
)

