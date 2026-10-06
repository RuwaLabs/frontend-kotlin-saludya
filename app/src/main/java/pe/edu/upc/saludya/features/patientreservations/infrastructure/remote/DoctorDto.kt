package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import com.google.gson.annotations.SerializedName

data class DoctorDto(
    val id: Int,
    @SerializedName("specialtyId")
    val specialtyId: Int,
    val name: String,
    val lastname: String
)
