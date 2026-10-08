package pe.edu.upc.saludya.notifications.domain

interface NotificationRepository {

    suspend fun getInbox(): List<AppNotification>

    suspend fun saveAll(notifications: List<AppNotification>)

    suspend fun markAsRead(id: Long)

    suspend fun delete(id: Long)
}
