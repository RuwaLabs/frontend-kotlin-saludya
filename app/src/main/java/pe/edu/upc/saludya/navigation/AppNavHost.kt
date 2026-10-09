package pe.edu.upc.saludya.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upc.saludya.features.iam.presentation.access.IamRoot
import pe.edu.upc.saludya.main.MainScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = IamRoute,
    ) {
        composable<IamRoute> {
            IamRoot(
                onAuthenticated = {
                    navController.navigate(MainRoute) {
                        popUpTo(IamRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<MainRoute> {
            MainScreen()
        }
    }
}
