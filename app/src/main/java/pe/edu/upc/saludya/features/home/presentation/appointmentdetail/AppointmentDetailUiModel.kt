package pe.edu.upc.saludya.features.home.presentation.appointmentdetail

import pe.edu.upc.saludya.core.desingsystem.component.AppointmentUiModel
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class AppointmentDetailUiModel(
    val title: String = "Detalle de cita",
    val appointment: AppointmentUiModel? = null,
    val reservation: InfoCardUiModel? = null,
    val registerArrivalText: String = "Registrar mi llegada",
    val cancelText: String = "Cancelar reserva",
    val note: String = "La cancelación está sujeta al plazo del establecimiento.",
)
