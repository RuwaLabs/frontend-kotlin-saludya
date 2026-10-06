package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

data class BookAppointmentRequestDto(
    val patientId: Int,
    val timeSlotId: Int
)
