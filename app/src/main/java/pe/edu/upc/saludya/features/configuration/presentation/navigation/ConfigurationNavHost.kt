package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import pe.edu.upc.saludya.R
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
import pe.edu.upc.saludya.navigation.ChangeEmailDestination
import pe.edu.upc.saludya.navigation.ChangePhoneDestination
import pe.edu.upc.saludya.navigation.ConfigurationDestination
import pe.edu.upc.saludya.navigation.ConfigurationGraph
import pe.edu.upc.saludya.navigation.LinkMinorDestination
import pe.edu.upc.saludya.navigation.LinkedMinorDestination
import pe.edu.upc.saludya.navigation.LinkedRelativesDestination
import pe.edu.upc.saludya.navigation.MinorHomeDestination
import pe.edu.upc.saludya.navigation.MyDataDestination
import pe.edu.upc.saludya.navigation.VerifyCodeDestination
import pe.edu.upc.saludya.navigation.VerifyIdentityDestination

fun NavGraphBuilder.configurationNavGraph(navController: NavController) {
    navigation<ConfigurationGraph>(startDestination = ConfigurationDestination) {

        composable<ConfigurationDestination> {
            ConfigurationScreen(
                onBackClick = { navController.popBackStack() },
                onMyDataClick = { navController.navigate(MyDataDestination) },
                onLinkedRelativesClick = { navController.navigate(LinkedRelativesDestination) },
                onChangePasswordClick = { /* TODO */ },
                onLogoutClick = { /* TODO */ },
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<MyDataDestination> {
            MyDataScreen(
                onBackClick = { navController.popBackStack() },
                onEditPhoneClick = { navController.navigate(ChangePhoneDestination) },
                onEditEmailClick = { navController.navigate(ChangeEmailDestination) },
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<ChangePhoneDestination> {
            ChangePhoneScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = {
                    navController.navigate(VerifyCodeDestination(ConfigurationDestinations.PHONE))
                },
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<ChangeEmailDestination> {
            ChangeEmailScreen(
                onBackClick = { navController.popBackStack() },
                onCodeSent = {
                    navController.navigate(VerifyCodeDestination(ConfigurationDestinations.EMAIL))
                },
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<VerifyCodeDestination> { entry ->
            val route = entry.toRoute<VerifyCodeDestination>()
            val descriptionText = when (route.destination) {
                ConfigurationDestinations.PHONE ->
                    stringResource(R.string.verify_code_description_phone)
                ConfigurationDestinations.EMAIL ->
                    stringResource(R.string.verify_code_description_email)
                else ->
                    stringResource(R.string.verify_code_description_default)
            }
            VerifyCodeScreen(
                descriptionText = descriptionText,
                onBackClick = { navController.popBackStack() },
                onVerified = {
                    navController.navigate(MyDataDestination) {
                        popUpTo(MyDataDestination) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBottomNavSelected = { /* TODO */ },
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
                onBottomNavSelected = { /* TODO */ },
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
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<LinkMinorDestination> {
            LinkMinorScreen(
                onBackClick = { navController.popBackStack() },
                onVerified = { _, _ ->
                    navController.navigate(VerifyIdentityDestination)
                },
                onBottomNavSelected = { /* TODO */ },
            )
        }

        composable<VerifyIdentityDestination> {
            VerifyIdentityScreen(
                onBackClick = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(MinorHomeDestination("1"))
                },
                onBottomNavSelected = { /* TODO */ },
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
                onBottomNavSelected = { /* TODO */ },
            )
        }
    }
}

private object ConfigurationDestinations {
    const val PHONE = "phone"
    const val EMAIL = "email"
}