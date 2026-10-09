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
import pe.edu.upc.saludya.features.patientreservations.presentation.navigation.PatientReservationsNavGraphRoute
import pe.edu.upc.saludya.features.patientreservations.presentation.navigation.patientReservationsNavGraph

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PatientReservationsNavGraphRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<InicioRoute> {
                PlaceholderScreen("Inicio")
            }
            patientReservationsNavGraph(navController)
            composable<AsistenciaRoute> {
                PlaceholderScreen("Asistencia")
            }
            composable<ConfiguracionRoute> {
                PlaceholderScreen("Configuración")
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
