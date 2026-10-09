package pe.edu.upc.saludya.notifications.presentation.notifications

import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationType

fun AppNotification.toInfoCard(): InfoCardUiModel = InfoCardUiModel(
    title = title,
    lines = listOf(message),
    footer = when (type) {
        NotificationType.REASSIGNMENT_OFFER -> "Ver oferta →"
        else -> null
    },
)
