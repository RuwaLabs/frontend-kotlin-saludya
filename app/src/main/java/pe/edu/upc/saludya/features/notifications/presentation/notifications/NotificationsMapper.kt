package pe.edu.upc.saludya.features.notifications.presentation.notifications

import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import pe.edu.upc.saludya.features.notifications.domain.NotificationType

fun AppNotification.toInfoCard(): InfoCardUiModel = InfoCardUiModel(
    title = title,
    lines = listOf(message),
    footer = when (type) {
        NotificationType.REASSIGNMENT_OFFER -> "Ver oferta →"
        else -> null
    },
)
