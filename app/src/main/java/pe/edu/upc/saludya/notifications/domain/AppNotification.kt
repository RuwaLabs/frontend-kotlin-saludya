package pe.edu.upc.saludya.notifications.domain

import java.time.Instant

data class AppNotification(
    val id: Long,
    val type: NotificationType,
    val title: String,
    val message: String,
    val createdAt: Instant?,
    val expiresAt: Instant? = null,
    val isRead: Boolean = false,
    val actionId: Long? = null
)
