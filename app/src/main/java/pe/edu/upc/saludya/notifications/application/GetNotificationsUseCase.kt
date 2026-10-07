package pe.edu.upc.saludya.notifications.application

import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<AppNotification> = repository.getInbox()
}
