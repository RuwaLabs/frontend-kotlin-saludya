package pe.edu.upc.saludya.home.presentation.filteredhistory

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class FilteredHistoryUiModel(
    val title: String = "Historial",
    val resultSummary: String = "25 sep 2026 · 1 resultado",
    val items: List<AppointmentUiModel> = listOf(
        AppointmentUiModel(
            specialty = "Medicina general",
            details = listOf(
                "Lucía Torres · Titular",
                "Dr. Luis Medina",
                "25 sep 2026 · 11:00–11:30",
            ),
            status = "Atendida",
        ),
    ),
    val changeDateText: String = "Cambiar fecha",
)
