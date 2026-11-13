package pe.edu.upc.saludya.features.home.presentation.historycalendar

import pe.edu.upc.saludya.core.desingsystem.component.CalendarUiModel

data class HistoryCalendarUiModel(
    val title: String = "Filtrar historial",
    val calendar: CalendarUiModel = CalendarUiModel(),
    val applyText: String = "Aplicar fecha",
    val clearText: String = "Quitar filtro",
)
