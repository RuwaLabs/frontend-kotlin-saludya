package pe.edu.upc.saludya.features.notifications.application

import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import pe.edu.upc.saludya.features.notifications.domain.NotificationRepository
import pe.edu.upc.saludya.features.reassignment.application.AcceptOfferByIdUseCase
import javax.inject.Inject

class AcceptReassignmentNotificationUseCase @Inject constructor(
    private val acceptOffer: AcceptOfferByIdUseCase,
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: AppNotification) {
        val offerId = notification.actionId ?: return
        val acceptedOffer = acceptOffer(offerId)
        if (acceptedOffer != null) {
            repository.markAsRead(notification.id)
        }
    }
}
