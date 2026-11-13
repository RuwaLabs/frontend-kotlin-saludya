package pe.edu.upc.saludya.features.home.presentation.filteredhistory

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class FilteredHistoryUiModel(
    val title: String = "Historial",
    val resultSummary: String = "Resultados",
    val items: List<AppointmentUiModel> = emptyList(),
    val changeDateText: String = "Cambiar fecha",
)
