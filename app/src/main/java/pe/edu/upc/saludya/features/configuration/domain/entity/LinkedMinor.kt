package pe.edu.upc.saludya.features.configuration.domain.entity

data class LinkedMinor(
    val id: String,
    val fullName: String,
    val age: Int,
    val documentType: String,
    val documentNumberMasked: String,
    val birthDate: String,
    val representativeName: String,
    val isVerified: Boolean,
)