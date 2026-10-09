package pe.edu.upc.saludya.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pe.edu.upc.saludya.features.configuration.presentation.navigation.ConfigurationNavHost
import pe.edu.upc.saludya.features.patientreservations.presentation.navigation.patientReservationsNavGraph
import pe.edu.upc.saludya.features.home.presentation.navigation.homeNavGraph
import pe.edu.upc.saludya.navigation.AsistenciaDestination
import pe.edu.upc.saludya.navigation.ConfiguracionDestination
import pe.edu.upc.saludya.navigation.HomeGraph
import pe.edu.upc.saludya.navigation.OfferAcceptedDestination
import pe.edu.upc.saludya.navigation.OfferDetailDestination
import pe.edu.upc.saludya.navigation.OfferRejectedDestination
import pe.edu.upc.saludya.navigation.navigateToAppDestination
import pe.edu.upc.saludya.features.notifications.presentation.navigation.notificationsNavGraph
import pe.edu.upc.saludya.features.reassignment.presentation.offerdetail.OfferDetailScreen

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { MainNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            homeNavGraph(navController)
            notificationsNavGraph(navController)
            patientReservationsNavGraph(navController)

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

            composable<AsistenciaDestination> {
                PlaceholderScreen("Asistencia")
            }
            composable<ConfiguracionDestination> {
                ConfigurationNavHost()
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}
