package pe.edu.upc.saludya.features.configuration.domain.entity

data class Patient(
    val id: String,
    val fullName: String,
    val role: String,
    val documentType: String,
    val documentNumberMasked: String,
    val phoneNumber: String,
    val email: String,
)