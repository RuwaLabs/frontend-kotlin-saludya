package pe.edu.upc.saludya.features.home.domain

data class HomeAppointment(
    val id: Long,
    val specialty: String,
    val patientName: String,
    val doctorName: String,
    val date: String,
    val time: String,
    val place: String,
    val status: String,
    val bookingCode: String,
    val isUpcoming: Boolean
)
