package pe.edu.upc.saludya.features.notifications.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pe.edu.upc.saludya.navigation.EmptyNotificationsDestination
import pe.edu.upc.saludya.navigation.NotificationsDestination
import pe.edu.upc.saludya.navigation.NotificationsGraph
import pe.edu.upc.saludya.navigation.OfferAcceptedDestination
import pe.edu.upc.saludya.navigation.OfferDetailDestination
import pe.edu.upc.saludya.navigation.OfferRejectedDestination
import pe.edu.upc.saludya.navigation.HomeDestination
import pe.edu.upc.saludya.navigation.navigateToAppDestination
import pe.edu.upc.saludya.features.notifications.domain.NotificationType
import pe.edu.upc.saludya.features.notifications.presentation.emptynotifications.EmptyNotificationsContent
import pe.edu.upc.saludya.features.notifications.presentation.notifications.NotificationsScreen
import pe.edu.upc.saludya.features.notifications.presentation.offeraccepted.OfferAcceptedScreen
import pe.edu.upc.saludya.features.notifications.presentation.offerrejected.OfferRejectedDialog

fun NavGraphBuilder.notificationsNavGraph(navController: NavController) {
    navigation<NotificationsGraph>(startDestination = NotificationsDestination) {

        composable<NotificationsDestination> {
            NotificationsScreen(
                onBackClick = { navController.popBackStack() },
                onNotificationClick = { notification ->
                    if (notification.type == NotificationType.REASSIGNMENT_OFFER &&
                        notification.actionId != null
                    ) {
                        navController.navigate(OfferDetailDestination(notification.actionId))
                    }
                },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<EmptyNotificationsDestination> {
            EmptyNotificationsContent(
                onBackClick = { navController.popBackStack() },
                onBackToHomeClick = {
                    navController.navigate(HomeDestination) { launchSingleTop = true }
                },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<OfferAcceptedDestination> {
            OfferAcceptedScreen(
                onBackClick = { navController.popBackStack() },
                onViewAppointmentClick = {
                    navController.navigate(HomeDestination) { launchSingleTop = true }
                },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<OfferRejectedDestination> {
            OfferRejectedDialog(
                onDismissRequest = { navController.popBackStack() },
                onViewAppointmentClick = {
                    navController.navigate(HomeDestination) { launchSingleTop = true }
                },
            )
        }
    }
}
