package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.saludya.features.configuration.presentation.changePhone.ChangePhoneScreen
import pe.edu.upc.saludya.features.configuration.presentation.configuration.ConfigurationScreen
import pe.edu.upc.saludya.features.configuration.presentation.myData.MyDataScreen

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
                onMyDataClick = {
                    navController.navigate(ConfigurationRoutes.MY_DATA)
                },
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

        composable(route = ConfigurationRoutes.MY_DATA) {
            MyDataScreen(
                onBackClick = { navController.popBackStack() },
                onEditPhoneClick = {
                    navController.navigate(ConfigurationRoutes.CHANGE_PHONE)
                },
                onEditEmailClick = { /* TODO: Fase 2 - Cambiar correo */ },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* TODO */ }
                        1 -> { /* TODO */ }
                        2 -> { /* TODO */ }
                        3 -> navController.navigate(ConfigurationRoutes.CONFIGURATION) {
                            popUpTo(ConfigurationRoutes.CONFIGURATION) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }

        composable(route = ConfigurationRoutes.CHANGE_PHONE) {
            ChangePhoneScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = { /* TODO: Fase 2 - Ir a verificar código */ },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* TODO */ }
                        1 -> { /* TODO */ }
                        2 -> { /* TODO */ }
                        3 -> navController.navigate(ConfigurationRoutes.CONFIGURATION) {
                            popUpTo(ConfigurationRoutes.CONFIGURATION) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}