package pe.edu.upc.saludya.home.presentation.home

import pe.edu.upc.saludya.home.domain.HomeAppointment

data class HomeUiState(
    val nextAppointment: HomeAppointment? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
