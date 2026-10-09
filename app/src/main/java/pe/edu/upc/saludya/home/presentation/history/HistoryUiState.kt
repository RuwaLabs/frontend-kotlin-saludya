package pe.edu.upc.saludya.home.presentation.history

import pe.edu.upc.saludya.home.domain.HomeAppointment

data class HistoryUiState(
    val items: List<HomeAppointment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
