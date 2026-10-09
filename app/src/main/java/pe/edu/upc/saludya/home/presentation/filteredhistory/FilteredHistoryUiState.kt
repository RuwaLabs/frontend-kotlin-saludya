package pe.edu.upc.saludya.home.presentation.filteredhistory

import pe.edu.upc.saludya.home.domain.HomeAppointment

data class FilteredHistoryUiState(
    val items: List<HomeAppointment> = emptyList(),
    val filterDate: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
