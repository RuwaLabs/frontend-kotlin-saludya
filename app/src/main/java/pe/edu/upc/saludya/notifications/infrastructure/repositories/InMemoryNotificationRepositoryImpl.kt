package pe.edu.upc.saludya.notifications.infrastructure.repositories

import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory inbox storage kept alive by Hilt as a single instance.
 *
 * saveAll merges (insert-or-update) instead of replacing, so the inbox accumulates history.
 * When an item already exists, the local read flag is preserved so syncing with the backend
 * does not mark a notification as unread again.
 */
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
