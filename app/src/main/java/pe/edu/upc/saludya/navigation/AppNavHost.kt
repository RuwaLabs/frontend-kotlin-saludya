package pe.edu.upc.saludya.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import pe.edu.upc.saludya.home.presentation.navigation.homeNavGraph
import pe.edu.upc.saludya.notifications.presentation.navigation.notificationsNavGraph
import pe.edu.upc.saludya.reassignment.presentation.offerdetail.OfferDetailScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeGraph,
    ) {
        homeNavGraph(navController)
        notificationsNavGraph(navController)

        composable<OfferDetailDestination> { entry ->
            val route = entry.toRoute<OfferDetailDestination>()
            OfferDetailScreen(
                offerId = route.offerId,
                onBackClick = { navController.popBackStack() },
                onAccepted = { navController.navigate(OfferAcceptedDestination) },
                onRejected = { navController.navigate(OfferRejectedDestination) },
                onDestinationClick = { navController.navigateToAppDestination(it) },
            )
        }
    }
}
