package pe.edu.upc.saludya.features.home.presentation.history

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class HistoryUiModel(
    val title: String = "Historial",
    val filterButtonText: String = "Filtrar por fecha",
    val items: List<AppointmentUiModel> = emptyList(),
)
