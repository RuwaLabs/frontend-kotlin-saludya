package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import pe.edu.upc.saludya.features.configuration.presentation.changeEmail.ChangeEmailScreen
import pe.edu.upc.saludya.features.configuration.presentation.changePassword.ChangePasswordScreen
import pe.edu.upc.saludya.features.configuration.presentation.changePhone.ChangePhoneScreen
import pe.edu.upc.saludya.features.configuration.presentation.configuration.ConfigurationScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkMinor.LinkMinorScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkedMinor.LinkedMinorScreen
import pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives.LinkedRelativesScreen
import pe.edu.upc.saludya.features.configuration.presentation.minorHome.MinorHomeScreen
import pe.edu.upc.saludya.features.configuration.presentation.myData.MyDataScreen
import pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity.VerifyIdentityScreen
import pe.edu.upc.saludya.navigation.ChangeEmailDestination
import pe.edu.upc.saludya.navigation.ChangePasswordDestination
import pe.edu.upc.saludya.navigation.ChangePhoneDestination
import pe.edu.upc.saludya.navigation.ConfigurationDestination
import pe.edu.upc.saludya.navigation.ConfigurationGraph
import pe.edu.upc.saludya.navigation.LinkMinorDestination
import pe.edu.upc.saludya.navigation.LinkedMinorDestination
import pe.edu.upc.saludya.navigation.LinkedRelativesDestination
import pe.edu.upc.saludya.navigation.MinorHomeDestination
import pe.edu.upc.saludya.navigation.MyDataDestination
import pe.edu.upc.saludya.navigation.VerifyIdentityDestination

fun NavGraphBuilder.configurationNavGraph(
    navController: NavController,
    onSessionEnded: () -> Unit = {},
) {
    navigation<ConfigurationGraph>(startDestination = ConfigurationDestination) {

        composable<ConfigurationDestination> {
            ConfigurationScreen(
                onBackClick = { navController.popBackStack() },
                onMyDataClick = { navController.navigate(MyDataDestination) },
                onLinkedRelativesClick = { navController.navigate(LinkedRelativesDestination) },
                onChangePasswordClick = { navController.navigate(ChangePasswordDestination) },
                onLogoutClick = { /* TODO */ },
            )
        }

        composable<MyDataDestination> {
            MyDataScreen(
                onBackClick = { navController.popBackStack() },
                onEditPhoneClick = { navController.navigate(ChangePhoneDestination) },
                onEditEmailClick = { navController.navigate(ChangeEmailDestination) },
            )
        }

        composable<ChangePhoneDestination> {
            ChangePhoneScreen(
                onBackClick = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(MyDataDestination) {
                        popUpTo(MyDataDestination) { inclusive = true }
                    }
                },
            )
        }

        composable<ChangeEmailDestination> {
            ChangeEmailScreen(
                onBackClick = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(MyDataDestination) {
                        popUpTo(MyDataDestination) { inclusive = true }
                    }
                },
            )
        }

        composable<ChangePasswordDestination> {
            ChangePasswordScreen(
                onBackClick = { navController.popBackStack() },
                onSessionEnded = onSessionEnded,
            )
        }

        composable<LinkedRelativesDestination> {
            LinkedRelativesScreen(
                onBackClick = { navController.popBackStack() },
                onViewMinorClick = { minorId ->
                    navController.navigate(LinkedMinorDestination(minorId))
                },
                onAddMinorClick = {
                    navController.navigate(LinkMinorDestination)
                },
            )
        }

        composable<LinkedMinorDestination> { entry ->
            val route = entry.toRoute<LinkedMinorDestination>()
            LinkedMinorScreen(
                minorId = route.minorId,
                onBackClick = { navController.popBackStack() },
                onViewAppointmentsClick = { /* TODO */ },
                onSyncClick = { /* TODO */ },
                onUnlinkConfirmed = {
                    navController.navigate(LinkedRelativesDestination) {
                        popUpTo(LinkedRelativesDestination) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<LinkMinorDestination> {
            LinkMinorScreen(
                onBackClick = { navController.popBackStack() },
                onVerified = { _, documentNumber ->
                    navController.navigate(VerifyIdentityDestination(documentNumber))
                },
            )
        }

        composable<VerifyIdentityDestination> { entry ->
            val route = entry.toRoute<VerifyIdentityDestination>()
            VerifyIdentityScreen(
                dni = route.dni,
                onBackClick = { navController.popBackStack() },
                onSuccess = { linkId ->
                    navController.navigate(MinorHomeDestination(linkId)) {
                        popUpTo(LinkedRelativesDestination) { inclusive = false }
                    }
                },
            )
        }

        composable<MinorHomeDestination> { entry ->
            val route = entry.toRoute<MinorHomeDestination>()
            MinorHomeScreen(
                minorId = route.minorId,
                onBackClick = { navController.popBackStack() },
                onViewAppointmentsClick = { /* TODO */ },
                onViewHistoryClick = { /* TODO */ },
                onBackToProfileClick = {
                    navController.navigate(ConfigurationDestination) {
                        popUpTo(ConfigurationDestination) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
