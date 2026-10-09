package pe.edu.upc.saludya.navigation

import kotlinx.serialization.Serializable

@Serializable data object IamRoute

@Serializable data object MainRoute

@Serializable data object HomeGraph

@Serializable data object HomeDestination
@Serializable data object PendingAppointmentsDestination
@Serializable data class AppointmentDetailDestination(val appointmentId: Long)
@Serializable data object HistoryDestination
@Serializable data object HistoryCalendarDestination
@Serializable data object FilteredHistoryDestination
@Serializable data object NoAppointmentsDestination
@Serializable data object LoadErrorDestination

@Serializable data object NotificationsGraph

@Serializable data object NotificationsDestination
@Serializable data object EmptyNotificationsDestination
@Serializable data object OfferAcceptedDestination
@Serializable data object OfferRejectedDestination

@Serializable data class OfferDetailDestination(val offerId: Long)

@Serializable data object AsistenciaDestination
@Serializable data object ConfiguracionDestination

// ===== Configuration (bounded context) =====

@Serializable data object ConfigurationGraph

@Serializable data object ConfigurationDestination
@Serializable data object MyDataDestination
@Serializable data object ChangePhoneDestination
@Serializable data object ChangeEmailDestination
@Serializable data object LinkedRelativesDestination
@Serializable data object LinkMinorDestination
@Serializable data object VerifyIdentityDestination

@Serializable data class VerifyCodeDestination(val destination: String)
@Serializable data class LinkedMinorDestination(val minorId: String)
@Serializable data class MinorHomeDestination(val minorId: String)