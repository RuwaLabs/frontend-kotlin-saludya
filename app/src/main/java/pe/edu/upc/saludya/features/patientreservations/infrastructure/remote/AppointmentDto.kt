package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import com.google.gson.annotations.SerializedName

data class AppointmentDto(
    val id: Int,
    val timeSlotId: Int,
    val patientId: Int,
    val bookingOrder: Int,
    val bookingCode: String,
    val status: String,
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
