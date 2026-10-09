package pe.edu.upc.saludya.notifications.application

import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import pe.edu.upc.saludya.notifications.domain.NotificationType
import pe.edu.upc.saludya.reassignment.application.GetPendingUseCase
import javax.inject.Inject

class SyncReassignmentNotificationsUseCase @Inject constructor(
    private val getPendingOffers: GetPendingUseCase,
    private val repository: NotificationRepository
) {
    suspend operator fun invoke() {
        val offers = getPendingOffers()
        val notifications = offers.map { offer ->
            AppNotification(
                id = offer.id,
                type = NotificationType.REASSIGNMENT_OFFER,
                title = "Se liberó un cupo",
                message = "Puedes adelantar tu cita. Acepta o rechaza la propuesta.",
                createdAt = offer.offeredAt,
                expiresAt = offer.expiresAt,
                actionId = offer.id
            )
        }
        repository.saveAll(notifications)
    }
}
