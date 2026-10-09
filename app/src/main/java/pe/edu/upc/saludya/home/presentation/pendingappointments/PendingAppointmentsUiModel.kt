package pe.edu.upc.saludya.home.presentation.pendingappointments

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class PendingAppointmentsUiModel(
    val title: String = "Citas pendientes",
    val appointments: List<AppointmentUiModel> = listOf(
        AppointmentUiModel(
            specialty = "Medicina general",
            details = listOf(
                "Lucía Torres · Titular",
                "Dra. Ana Ríos",
                "Mar 6 oct 2026 · 09:00–09:30",
                "Centro de Salud · Consultorio 02",
            ),
            status = "Confirmada",
        ),
        AppointmentUiModel(
            specialty = "Pediatría",
            details = listOf(
                "Mateo Torres · Menor vinculado",
                "Dra. Carla Díaz",
                "Mié 7 oct · 10:00–10:30",
            ),
            status = "Confirmada",
        ),
    ),
    val hint: String = "Selecciona una cita para ver su detalle.",
)
