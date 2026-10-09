package pe.edu.upc.saludya.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upc.saludya.main.MainRoute
import pe.edu.upc.saludya.main.MainScreen

/**
 * NavHost raíz de la app.
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<MainRoute> {
                MainScreen()
            }
        }
    }
}
