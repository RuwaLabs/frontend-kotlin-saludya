package pe.edu.upc.saludya.notifications.application

import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import pe.edu.upc.saludya.reassignment.application.RejectOfferByIdUseCase
import javax.inject.Inject

/**
 * Handles the "Reject" action of a REASSIGNMENT_OFFER notification.
 * Delegates the domain decision to reassignment and marks the inbox item as read on success.
 */
class RejectReassignmentNotificationUseCase @Inject constructor(
    private val rejectOffer: RejectOfferByIdUseCase,
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: AppNotification) {
        val offerId = notification.actionId ?: return
        val rejectedOffer = rejectOffer(offerId)
        if (rejectedOffer != null) {
            repository.markAsRead(notification.id)
        }
    }
}
