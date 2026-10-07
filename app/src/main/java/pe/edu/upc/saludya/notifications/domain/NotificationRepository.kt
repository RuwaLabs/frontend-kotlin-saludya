package pe.edu.upc.saludya.notifications.domain

/**
 * Local inbox storage. Deliberately does not depend on any other feature.
 * The current implementation is in-memory; a Room-backed one can replace it without
 * touching the use cases or the UI.
 */
interface NotificationRepository {

    suspend fun getInbox(): List<AppNotification>

    suspend fun saveAll(notifications: List<AppNotification>)

    suspend fun markAsRead(id: Long)

    suspend fun delete(id: Long)
}
