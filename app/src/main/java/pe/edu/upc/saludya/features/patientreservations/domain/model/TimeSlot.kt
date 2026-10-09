package pe.edu.upc.saludya.features.patientreservations.domain.model

data class TimeSlot(
    val id : Int,
    val doctorId : Int,
    val date : String,
    val startHour : String,
    val endHour : String,
    val room : String,
    val maxCapacity : Int,
    val currentBookings : Int,
    val status : String
)
