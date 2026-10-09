package pe.edu.upc.saludya.features.home.presentation.history

import pe.edu.upc.saludya.features.home.domain.HomeAppointment

data class HistoryUiState(
    val items: List<HomeAppointment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
