package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

data class TimeSlotDto(
    val id: Int,
    val doctorId: Int,
    val date: String,
    val startHour: String,
    val endHour: String,
    val room: String,
    val maxCapacity: Int,
    val currentBookings: Int,
    val status: String
)
