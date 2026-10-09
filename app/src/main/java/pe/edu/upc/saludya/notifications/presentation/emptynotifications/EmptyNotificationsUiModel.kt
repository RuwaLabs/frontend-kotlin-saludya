package pe.edu.upc.saludya.notifications.presentation.emptynotifications

data class EmptyNotificationsUiModel(
    val title: String = "Notificaciones",
    val heading: String = "Todo al día",
    val description: String = "Aquí aparecerán tus confirmaciones, ofertas y avisos de atención.",
    val buttonText: String = "Volver al inicio",
)
