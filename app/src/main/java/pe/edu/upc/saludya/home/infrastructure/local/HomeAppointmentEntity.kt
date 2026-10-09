package pe.edu.upc.saludya.home.infrastructure.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "home_appointments")
data class HomeAppointmentEntity(
    @PrimaryKey val id: Long,
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
