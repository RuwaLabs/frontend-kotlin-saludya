package pe.edu.upc.saludya.home.presentation.appointmentdetail

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class AppointmentDetailUiModel(
    val title: String = "Detalle de cita",
    val appointment: AppointmentUiModel = AppointmentUiModel(
        specialty = "Medicina general",
        details = listOf(
            "Lucía Torres · Titular",
            "Dra. Ana Ríos",
            "Mar 6 oct 2026 · 09:00–09:30",
            "Centro de Salud · Consultorio 02",
        ),
        status = "Confirmada",
    ),
    val reservation: InfoCardUiModel = InfoCardUiModel(
        title = "Código de reserva",
        lines = listOf(
            "SY-261006-0142",
            "Beneficiario: Lucía Torres",
            "Comprobante enviado a tu correo.",
        ),
    ),
    val registerArrivalText: String = "Registrar mi llegada",
    val cancelText: String = "Cancelar reserva",
    val note: String = "La cancelación está sujeta al plazo del establecimiento.",
)
