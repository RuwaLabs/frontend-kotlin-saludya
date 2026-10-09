package pe.edu.upc.saludya.home.presentation.pendingappointments

import pe.edu.upc.saludya.home.domain.HomeAppointment

data class PendingAppointmentsUiState(
    val appointments: List<HomeAppointment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
