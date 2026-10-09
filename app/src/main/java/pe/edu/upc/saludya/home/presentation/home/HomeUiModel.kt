package pe.edu.upc.saludya.home.presentation.home

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class HomeUiModel(
    val greeting: String = "Hola, Lucía",
    val subtitle: String = "¿Cómo podemos ayudarte hoy?",
    val pendingTitle: String = "Citas pendientes",
    val nextAppointment: AppointmentUiModel = AppointmentUiModel(
        specialty = "Medicina general",
        details = listOf(
            "Lucía Torres · Titular",
            "Dra. Ana Ríos",
            "Mar 6 oct 2026 · 09:00–09:30",
            "Centro de Salud · Consultorio 02",
        ),
        status = "Confirmada",
    ),
    val viewAppointmentsText: String = "Ver mis citas",
    val history: InfoCardUiModel = InfoCardUiModel(
        title = "Historial",
        lines = listOf("Tus últimas atenciones y citas finalizadas."),
        footer = "Ver historial →",
    ),
    val reserveText: String = "Reservar una cita",
)
