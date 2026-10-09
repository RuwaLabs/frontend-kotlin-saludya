package pe.edu.upc.saludya.features.home.presentation.history

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class HistoryUiModel(
    val title: String = "Historial",
    val filterButtonText: String = "Filtrar por fecha",
    val items: List<AppointmentUiModel> = listOf(
        AppointmentUiModel(
            specialty = "Medicina general",
            details = listOf("Lucía Torres · 25 sep 2026", "Dr. Luis Medina"),
            status = "Atendida",
        ),
        AppointmentUiModel(
            specialty = "Odontología",
            details = listOf("Lucía Torres · 18 sep 2026"),
            status = "Cancelada",
        ),
    ),
)
