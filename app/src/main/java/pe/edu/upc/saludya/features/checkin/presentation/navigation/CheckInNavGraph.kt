package pe.edu.upc.saludya.features.checkin.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import pe.edu.upc.saludya.features.checkin.presentation.called.TurnCalledScreen
import pe.edu.upc.saludya.features.checkin.presentation.qr.QrCodeScreen
import pe.edu.upc.saludya.features.checkin.presentation.queue.QueueScreen
import pe.edu.upc.saludya.features.checkin.presentation.ticket.TicketScreen
import pe.edu.upc.saludya.navigation.AppointmentDetailDestination
import pe.edu.upc.saludya.navigation.HomeGraph

@Serializable data object CheckInGraph
@Serializable data object CheckInQrDestination
@Serializable data class CheckInTicketDestination(val appointmentId: Int)
@Serializable data class CheckInQueueDestination(val appointmentId: Int)
@Serializable data class CheckInCalledDestination(val appointmentId: Int)

fun NavGraphBuilder.checkInNavGraph(navController: NavController) {
    val goHome = {
        navController.navigate(HomeGraph) {
            launchSingleTop = true
            popUpTo(navController.graph.id) { inclusive = false }
        }
    }

    navigation<CheckInGraph>(startDestination = CheckInQrDestination) {

        composable<CheckInQrDestination> {
            QrCodeScreen(
                onBackClick = { navController.popBackStack() },
                onViewReservationDetail = { appointmentId ->
                    navController.navigate(AppointmentDetailDestination(appointmentId.toLong()))
                },
                onArrivalRegistered = { appointmentId ->
                    navController.navigate(CheckInTicketDestination(appointmentId))
                },
                onBottomNavItemSelected = { },
            )
        }

        composable<CheckInTicketDestination> { entry ->
            val route = entry.toRoute<CheckInTicketDestination>()
            TicketScreen(
                appointmentId = route.appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewPosition = { navController.navigate(CheckInQueueDestination(route.appointmentId)) },
                onAttended = goHome,
                onBottomNavItemSelected = { },
            )
        }

        composable<CheckInQueueDestination> { entry ->
            val route = entry.toRoute<CheckInQueueDestination>()
            QueueScreen(
                appointmentId = route.appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewTicket = {
                    navController.navigate(CheckInTicketDestination(route.appointmentId)) { launchSingleTop = true }
                },
                onTurnCalled = {
                    navController.navigate(CheckInCalledDestination(route.appointmentId)) {
                        popUpTo<CheckInQueueDestination> { inclusive = true }
                    }
                },
                onLeftQueue = { navController.popBackStack() },
                onAttended = goHome,
                onBottomNavItemSelected = { },
            )
        }

        composable<CheckInCalledDestination> { entry ->
            val route = entry.toRoute<CheckInCalledDestination>()
            TurnCalledScreen(
                appointmentId = route.appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewTicket = {
                    navController.navigate(CheckInTicketDestination(route.appointmentId)) { launchSingleTop = true }
                },
                onAttended = goHome,
                onBottomNavItemSelected = { },
            )
        }
    }
}
