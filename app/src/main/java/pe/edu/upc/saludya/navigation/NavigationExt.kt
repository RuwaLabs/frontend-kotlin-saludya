package pe.edu.upc.saludya.navigation

import androidx.navigation.NavController
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination

fun NavController.navigateToAppDestination(destination: SaludYaDestination) {
    when (destination) {
        SaludYaDestination.Inicio -> navigate(HomeDestination) { launchSingleTop = true }
        else -> Unit
    }
}
