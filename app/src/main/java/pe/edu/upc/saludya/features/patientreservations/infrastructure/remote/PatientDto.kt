package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

data class PatientDto(
    val id: Int,
    val userId: Int,
    val dni: String,
    val name: String,
    val lastname: String,
    val birthDate: String?,
    val phone: String?
)

data class PatientMinorDto(
    val id: Int,
    val patientId: Int,
    val tutorId: Int
)
