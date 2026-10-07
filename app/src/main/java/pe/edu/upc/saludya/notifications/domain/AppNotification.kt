package pe.edu.upc.saludya.notifications.domain

import java.time.Instant

/**
 * A single inbox item. It is intentionally generic: it does not know about reassignment.
 * When a notification is actionable, [actionId] carries the id of the target resource
 * (for REASSIGNMENT_OFFER it is the offer id) and [type] tells the UI what to do with it.
 */
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
