package pe.edu.upc.saludya.features.home.presentation.appointmentdetail

import pe.edu.upc.saludya.features.home.domain.HomeAppointment

data class AppointmentDetailUiState(
    val appointment: HomeAppointment? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
