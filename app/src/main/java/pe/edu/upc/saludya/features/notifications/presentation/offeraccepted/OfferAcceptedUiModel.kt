package pe.edu.upc.saludya.features.notifications.presentation.offeraccepted

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel

data class OfferAcceptedUiModel(
    val title: String = "Horario actualizado",
    val headline: String = "¡Tu cita fue reasignada!",
    val appointment: AppointmentUiModel = AppointmentUiModel(
        specialty = "Medicina general",
        details = listOf(
            "Lucía Torres",
            "6 oct · 08:30–09:00",
            "Dra. Ana Ríos · Consultorio 02",
        ),
        status = "Confirmada",
    ),
    val note: String = "El horario anterior quedó liberado. Enviamos el nuevo comprobante a tu correo.",
    val viewAppointmentText: String = "Ver mi cita",
)
