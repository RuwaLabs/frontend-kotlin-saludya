package pe.edu.upc.saludya.features.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import pe.edu.upc.saludya.features.home.presentation.appointmentdetail.AppointmentDetailScreen
import pe.edu.upc.saludya.features.home.presentation.filteredhistory.FilteredHistoryScreen
import pe.edu.upc.saludya.features.home.presentation.history.HistoryScreen
import pe.edu.upc.saludya.features.home.presentation.historycalendar.HistoryCalendarScreen
import pe.edu.upc.saludya.features.home.presentation.home.HomeScreen
import pe.edu.upc.saludya.features.home.presentation.loaderror.LoadErrorScreen
import pe.edu.upc.saludya.features.home.presentation.noappointments.NoAppointmentsScreen
import pe.edu.upc.saludya.features.home.presentation.pendingappointments.PendingAppointmentsScreen
import pe.edu.upc.saludya.navigation.AppointmentDetailDestination
import pe.edu.upc.saludya.navigation.FilteredHistoryDestination
import pe.edu.upc.saludya.navigation.HistoryCalendarDestination
import pe.edu.upc.saludya.navigation.HistoryDestination
import pe.edu.upc.saludya.navigation.HomeDestination
import pe.edu.upc.saludya.navigation.HomeGraph
import pe.edu.upc.saludya.navigation.LoadErrorDestination
import pe.edu.upc.saludya.navigation.NoAppointmentsDestination
import pe.edu.upc.saludya.navigation.NotificationsGraph
import pe.edu.upc.saludya.navigation.PendingAppointmentsDestination
import pe.edu.upc.saludya.navigation.navigateToAppDestination

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    navigation<HomeGraph>(startDestination = HomeDestination) {

        composable<HomeDestination> {
            HomeScreen(
                onNotificationsClick = { navController.navigate(NotificationsGraph) },
                onAppointmentClick = { id -> navController.navigate(AppointmentDetailDestination(id)) },
                onViewAppointmentsClick = { navController.navigate(PendingAppointmentsDestination) },
                onViewHistoryClick = { navController.navigate(HistoryDestination) },
                onReserveClick = { },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<PendingAppointmentsDestination> {
            PendingAppointmentsScreen(
                onBackClick = { navController.popBackStack() },
                onAppointmentClick = { id -> navController.navigate(AppointmentDetailDestination(id)) },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<AppointmentDetailDestination> { entry ->
            val route = entry.toRoute<AppointmentDetailDestination>()
            AppointmentDetailScreen(
                appointmentId = route.appointmentId,
                onBackClick = { navController.popBackStack() },
                onRegisterArrivalClick = { },
                onCancelClick = { },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<HistoryDestination> {
            HistoryScreen(
                onBackClick = { navController.popBackStack() },
                onFilterClick = { navController.navigate(HistoryCalendarDestination) },
                onItemClick = { id -> navController.navigate(AppointmentDetailDestination(id)) },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<HistoryCalendarDestination> {
            HistoryCalendarScreen(
                onBackClick = { navController.popBackStack() },
                onApplyClick = { navController.navigate(FilteredHistoryDestination) },
                onClearClick = { navController.popBackStack() },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<FilteredHistoryDestination> {
            FilteredHistoryScreen(
                onBackClick = { navController.popBackStack() },
                onItemClick = { id -> navController.navigate(AppointmentDetailDestination(id)) },
                onChangeDateClick = { navController.navigate(HistoryCalendarDestination) },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<NoAppointmentsDestination> {
            NoAppointmentsScreen(
                onBackClick = { navController.popBackStack() },
                onReserveClick = { },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }

        composable<LoadErrorDestination> {
            LoadErrorScreen(
                onBackClick = { navController.popBackStack() },
                onRetryClick = { },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }
    }
}
