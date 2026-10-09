package pe.edu.upc.saludya.notifications.presentation.notifications

import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel

data class NotificationsUiModel(
    val title: String = "Notificaciones",
    val notifications: List<InfoCardUiModel> = listOf(
        InfoCardUiModel(
            title = "Horario más temprano disponible",
            lines = listOf("Medicina general · 6 oct", "Oferta recibida a las 08:00"),
            footer = "Ver oferta →",
        ),
        InfoCardUiModel(
            title = "Reserva confirmada",
            lines = listOf("Tu cita está reservada para el 6 oct a las 09:00."),
            footer = "Hace 15 min",
        ),
        InfoCardUiModel(
            title = "Es tu turno",
            lines = listOf("Acércate al consultorio 02 cuando recibas tu llamada."),
            footer = "Ejemplo de aviso de atención",
        ),
    ),
)
