package pe.edu.upc.saludya.notifications.application

import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import pe.edu.upc.saludya.reassignment.application.AcceptOfferByIdUseCase
import javax.inject.Inject

/**
 * Handles the "Accept" action of a REASSIGNMENT_OFFER notification.
 * Delegates the domain decision to reassignment and marks the inbox item as read on success.
 */
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
