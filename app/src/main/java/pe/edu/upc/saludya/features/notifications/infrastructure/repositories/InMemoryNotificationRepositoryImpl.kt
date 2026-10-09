package pe.edu.upc.saludya.features.notifications.infrastructure.repositories

import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import pe.edu.upc.saludya.features.notifications.domain.NotificationRepository
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryNotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    private var inbox: List<AppNotification> = emptyList()

    override suspend fun getInbox(): List<AppNotification> = inbox

    override suspend fun saveAll(notifications: List<AppNotification>) {
        val byId = inbox.associateBy { it.id }.toMutableMap()
        notifications.forEach { incoming ->
            val existing = byId[incoming.id]
            byId[incoming.id] = if (existing != null) {
                incoming.copy(isRead = existing.isRead)
            } else {
                incoming
            }
        }
        inbox = byId.values.sortedByDescending { it.createdAt ?: Instant.EPOCH }
    }

    override suspend fun markAsRead(id: Long) {
        inbox = inbox.map { if (it.id == id) it.copy(isRead = true) else it }
    }

    override suspend fun delete(id: Long) {
        inbox = inbox.filterNot { it.id == id }
    }
}
