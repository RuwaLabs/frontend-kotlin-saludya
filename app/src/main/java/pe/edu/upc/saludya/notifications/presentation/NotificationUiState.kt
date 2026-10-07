package pe.edu.upc.saludya.notifications.presentation

import pe.edu.upc.saludya.notifications.domain.AppNotification

data class NotificationUiState(
    val notifications: List<AppNotification> = emptyList(),
    val unreadCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
