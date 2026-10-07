package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.edu.upc.saludya.features.configuration.presentation.changeEmail.ChangeEmailScreen
import pe.edu.upc.saludya.features.configuration.presentation.changePhone.ChangePhoneScreen
import pe.edu.upc.saludya.features.configuration.presentation.configuration.ConfigurationScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives.LinkedRelativesScreen
import pe.edu.upc.saludya.features.configuration.presentation.myData.MyDataScreen
import pe.edu.upc.saludya.features.configuration.presentation.verifyCode.VerifyCodeScreen

@Composable
fun ConfigurationNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ConfigurationRoutes.CONFIGURATION,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // H01 - Configuración
        composable(route = ConfigurationRoutes.CONFIGURATION) {
            ConfigurationScreen(
                onBackClick = { /* Pantalla raíz: sin acción */ },
                onMyDataClick = {
                    navController.navigate(ConfigurationRoutes.MY_DATA)
                },
                onLinkedRelativesClick = {
                    navController.navigate(ConfigurationRoutes.LINKED_RELATIVES)
                },
                onChangePasswordClick = { /* TODO */ },
                onLogoutClick = { /* TODO */ },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* TODO */ }
                        1 -> { /* TODO */ }
                        2 -> { /* TODO */ }
                        3 -> { /* Configuración: ya estamos aquí */ }
                    }
                },
            )
        }

        // H02 - Mis datos
        composable(route = ConfigurationRoutes.MY_DATA) {
            MyDataScreen(
                onBackClick = { navController.popBackStack() },
                onEditPhoneClick = {
                    navController.navigate(ConfigurationRoutes.CHANGE_PHONE)
                },
                onEditEmailClick = {
                    navController.navigate(ConfigurationRoutes.CHANGE_EMAIL)
                },
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

        // H05 - Cambiar celular
        composable(route = ConfigurationRoutes.CHANGE_PHONE) {
            ChangePhoneScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = {
                    navController.navigate(
                        ConfigurationRoutes.verifyCode(ConfigurationRoutes.DEST_PHONE)
                    )
                },
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

        // H06 - Cambiar correo
        composable(route = ConfigurationRoutes.CHANGE_EMAIL) {
            ChangeEmailScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = {
                    navController.navigate(
                        ConfigurationRoutes.verifyCode(ConfigurationRoutes.DEST_EMAIL)
                    )
                },
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

        // H07 - Verificar código (con argumento destination)
        composable(
            route = ConfigurationRoutes.VERIFY_CODE_WITH_ARG,
            arguments = listOf(
                navArgument(ConfigurationRoutes.ARG_DESTINATION) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val destination = backStackEntry.arguments
                ?.getString(ConfigurationRoutes.ARG_DESTINATION)
                .orEmpty()

            val descriptionText = when (destination) {
                ConfigurationRoutes.DEST_PHONE ->
                    "Ingresa el código de 6 dígitos enviado a tu correo registrado para confirmar el cambio de celular."
                ConfigurationRoutes.DEST_EMAIL ->
                    "Ingresa el código de 6 dígitos enviado a l••••a@correo.com."
                else ->
                    "Ingresa el código de 6 dígitos enviado."
            }

            VerifyCodeScreen(
                descriptionText = descriptionText,
                onBackClick = { navController.popBackStack() },
                onVerified = { /* TODO: Fase 2 - Ir a "Cambio realizado" (modal) */ },
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

        // H08 - Parientes vinculados
        composable(route = ConfigurationRoutes.LINKED_RELATIVES) {
            LinkedRelativesScreen(
                onBackClick = { navController.popBackStack() },
                onViewMinorClick = { /* TODO: Fase 2 - Ir a "Menor vinculado" */ },
                onAddMinorClick = { /* TODO: Fase 2 - Ir a "Vincular menor" */ },
                onSyncClick = { /* TODO: Fase 2 - Sincronizar */ },
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