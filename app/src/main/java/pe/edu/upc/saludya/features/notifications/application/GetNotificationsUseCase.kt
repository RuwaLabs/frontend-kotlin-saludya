package pe.edu.upc.saludya.features.notifications.application

import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import pe.edu.upc.saludya.features.notifications.domain.NotificationRepository
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<AppNotification> = repository.getInbox()
}
