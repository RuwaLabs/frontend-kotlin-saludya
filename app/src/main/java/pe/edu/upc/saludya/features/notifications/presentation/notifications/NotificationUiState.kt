package pe.edu.upc.saludya.features.notifications.presentation.notifications

import pe.edu.upc.saludya.features.notifications.domain.AppNotification

data class NotificationUiState(
    val notifications: List<AppNotification> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
