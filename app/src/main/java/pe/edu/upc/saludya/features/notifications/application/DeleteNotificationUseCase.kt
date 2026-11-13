package pe.edu.upc.saludya.features.notifications.application

import pe.edu.upc.saludya.features.notifications.domain.NotificationRepository
import javax.inject.Inject

class DeleteNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
