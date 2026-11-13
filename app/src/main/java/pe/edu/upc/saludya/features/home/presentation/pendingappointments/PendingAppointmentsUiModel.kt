package pe.edu.upc.saludya.features.home.presentation.pendingappointments

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class PendingAppointmentsUiModel(
    val title: String = "Citas pendientes",
    val appointments: List<AppointmentUiModel> = emptyList(),
    val hint: String = "Selecciona una cita para ver su detalle.",
)
