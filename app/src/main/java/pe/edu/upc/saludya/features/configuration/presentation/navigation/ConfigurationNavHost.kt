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
import pe.edu.upc.saludya.features.configuration.presentation.linkMinor.LinkMinorScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkedMinor.LinkedMinorScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives.LinkedRelativesScreen
import pe.edu.upc.saludya.features.configuration.presentation.minorHome.MinorHomeScreen
import pe.edu.upc.saludya.features.configuration.presentation.myData.MyDataScreen
import pe.edu.upc.saludya.features.configuration.presentation.verifyCode.VerifyCodeScreen
import pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity.VerifyIdentityScreen

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
                onMyDataClick = { navController.navigate(ConfigurationRoutes.MY_DATA) },
                onLinkedRelativesClick = { navController.navigate(ConfigurationRoutes.LINKED_RELATIVES) },
                onChangePasswordClick = { /* TODO */ },
                onLogoutClick = { /* TODO */ },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* TODO */ }
                        1 -> { /* TODO */ }
                        2 -> { /* TODO */ }
                        3 -> { /* Configuración */ }
                    }
                },
            )
        }

        // H02 - Mis datos
        composable(route = ConfigurationRoutes.MY_DATA) {
            MyDataScreen(
                onBackClick = { navController.popBackStack() },
                onEditPhoneClick = { navController.navigate(ConfigurationRoutes.CHANGE_PHONE) },
                onEditEmailClick = { navController.navigate(ConfigurationRoutes.CHANGE_EMAIL) },
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
                    navController.navigate(ConfigurationRoutes.verifyCode(ConfigurationRoutes.DEST_PHONE))
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
                    navController.navigate(ConfigurationRoutes.verifyCode(ConfigurationRoutes.DEST_EMAIL))
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

        // H07 - Verificar código
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
                onVerified = {
                    navController.navigate(ConfigurationRoutes.MY_DATA) {
                        popUpTo(ConfigurationRoutes.MY_DATA) { inclusive = false }
                        launchSingleTop = true
                    }
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

        // H08 - Parientes vinculados
        composable(route = ConfigurationRoutes.LINKED_RELATIVES) {
            LinkedRelativesScreen(
                onBackClick = { navController.popBackStack() },
                onViewMinorClick = { minorId ->
                    navController.navigate(ConfigurationRoutes.linkedMinor(minorId))
                },
                onAddMinorClick = {
                    navController.navigate(ConfigurationRoutes.LINK_MINOR)
                },
                onSyncClick = { /* TODO */ },
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

        // H09 - Menor vinculado
        composable(
            route = ConfigurationRoutes.LINKED_MINOR_WITH_ARG,
            arguments = listOf(
                navArgument(ConfigurationRoutes.ARG_MINOR_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val minorId = backStackEntry.arguments
                ?.getString(ConfigurationRoutes.ARG_MINOR_ID)
                .orEmpty()

            LinkedMinorScreen(
                minorId = minorId,
                onBackClick = { navController.popBackStack() },
                onViewAppointmentsClick = { /* TODO */ },
                onSyncClick = { /* TODO */ },
                onUnlinkConfirmed = {
                    // Vuelve a Parientes vinculados (ya sin este menor)
                    navController.navigate(ConfigurationRoutes.LINKED_RELATIVES) {
                        popUpTo(ConfigurationRoutes.LINKED_RELATIVES) { inclusive = true }
                        launchSingleTop = true
                    }
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

        // H10 - Vincular menor
        composable(route = ConfigurationRoutes.LINK_MINOR) {
            LinkMinorScreen(
                onBackClick = { navController.popBackStack() },
                onVerified = { _, _ ->
                    navController.navigate(ConfigurationRoutes.VERIFY_IDENTITY)
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

        // H11 - Verificar identidad
        composable(route = ConfigurationRoutes.VERIFY_IDENTITY) {
            VerifyIdentityScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    // TODO: Cuando exista el modal "Vinculación completada", navegar ahí primero
                    // Por ahora vamos directo al home del menor recién vinculado
                    navController.navigate(ConfigurationRoutes.minorHome("1"))
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

        // H12 - Home del menor
        composable(
            route = ConfigurationRoutes.MINOR_HOME_WITH_ARG,
            arguments = listOf(
                navArgument(ConfigurationRoutes.ARG_MINOR_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val minorId = backStackEntry.arguments
                ?.getString(ConfigurationRoutes.ARG_MINOR_ID)
                .orEmpty()

            MinorHomeScreen(
                minorId = minorId,
                onBackClick = { navController.popBackStack() },
                onViewAppointmentsClick = { /* TODO */ },
                onViewHistoryClick = { /* TODO */ },
                onBackToProfileClick = {
                    navController.navigate(ConfigurationRoutes.CONFIGURATION) {
                        popUpTo(ConfigurationRoutes.CONFIGURATION) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* Inicio: ya estamos aquí */ }
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