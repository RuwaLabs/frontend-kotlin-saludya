package pe.edu.upc.saludya.notifications.application

import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long) = repository.markAsRead(id)
}
