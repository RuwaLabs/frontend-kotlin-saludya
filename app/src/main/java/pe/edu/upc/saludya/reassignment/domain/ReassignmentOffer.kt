package pe.edu.upc.saludya.reassignment.domain

import java.time.Instant

data class ReassignmentOffer(
    val id: Int,
    val appointmentId: Long,
    val freedTimeSlotId: Long,
    val status: String,
    val expiresAt: Instant
)

