package pe.edu.upc.saludya.features.home.presentation.home

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class HomeUiModel(
    val greeting: String = "Hola",
    val subtitle: String = "¿Cómo podemos ayudarte hoy?",
    val pendingTitle: String = "Citas pendientes",
    val nextAppointment: AppointmentUiModel? = null,
    val viewAppointmentsText: String = "Ver mis citas",
    val history: InfoCardUiModel = InfoCardUiModel(
        title = "Historial",
        lines = listOf("Tus últimas atenciones y citas finalizadas."),
        footer = "Ver historial →",
    ),
    val reserveText: String = "Reservar una cita",
)
