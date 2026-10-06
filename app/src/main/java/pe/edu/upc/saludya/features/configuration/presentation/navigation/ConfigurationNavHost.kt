package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.saludya.features.configuration.presentation.configuration.ConfigurationScreen

@Composable
fun ConfigurationNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ConfigurationRoutes.CONFIGURATION,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = ConfigurationRoutes.CONFIGURATION) {
            ConfigurationScreen(
                onBackClick = { /* Pantalla raíz: sin acción */ },
                onMyDataClick = { /* TODO: Fase 2 - Mis datos */ },
                onLinkedRelativesClick = { /* TODO: Fase 2 - Parientes vinculados */ },
                onChangePasswordClick = { /* TODO: Fase 2 - Cambiar contraseña */ },
                onLogoutClick = { /* TODO: Fase 2 - Cerrar sesión */ },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* TODO: Home del paciente */ }
                        1 -> { /* TODO: Reservar cita */ }
                        2 -> { /* TODO: Asistencia */ }
                        3 -> { /* Configuración: ya estamos aquí */ }
                    }
                },
            )
        }
    }
}