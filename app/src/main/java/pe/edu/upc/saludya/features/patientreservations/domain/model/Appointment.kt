package pe.edu.upc.saludya.features.patientreservations.domain.model

data class Appointment(
    val id: Int,
    val timeSlotId: Int,
    val patientId: Int,
    val bookingOrder: Int,
    val bookingCode: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
