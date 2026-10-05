package pe.edu.upc.saludya.features.configuration.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.edu.upc.saludya.features.configuration.presentation.additionalRules.AdditionalRulesScreen
import pe.edu.upc.saludya.features.configuration.presentation.attentionIntervals.AttentionIntervalsScreen
import pe.edu.upc.saludya.features.configuration.presentation.configuration.ConfigurationScreen
import pe.edu.upc.saludya.features.configuration.presentation.dailyIndicators.DailyIndicatorsScreen
import pe.edu.upc.saludya.features.configuration.presentation.editRule.EditRuleScreen
import pe.edu.upc.saludya.features.configuration.presentation.exportReport.ExportReportScreen
import pe.edu.upc.saludya.features.configuration.presentation.specialtyDemand.SpecialtyDemandScreen

@Composable
fun ConfigurationNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ConfigurationRoutes.CONFIGURATION,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // H01 - Configuración general
        composable(route = ConfigurationRoutes.CONFIGURATION) {
            ConfigurationScreen(
                onBackClick = null,
                onSeeMoreRulesClick = {
                    navController.navigate(ConfigurationRoutes.ADDITIONAL_RULES)
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS)
                        1 -> { /* TODO: Calendario */ }
                        2 -> { /* TODO: Cita */ }
                        3 -> { /* Configuración: ya estamos aquí */ }
                    }
                },
            )
        }

        // H02 - Más reglas
        composable(route = ConfigurationRoutes.ADDITIONAL_RULES) {
            AdditionalRulesScreen(
                onBackClick = { navController.popBackStack() },
                onIntervalsClick = {
                    navController.navigate(ConfigurationRoutes.ATTENTION_INTERVALS)
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS)
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

        // H13 - Intervalos de atención
        composable(route = ConfigurationRoutes.ATTENTION_INTERVALS) {
            AttentionIntervalsScreen(
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS)
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

        // H03 - Editar regla (pantalla completa)
        composable(
            route = ConfigurationRoutes.EDIT_RULE_WITH_ARG,
            arguments = listOf(
                navArgument(ConfigurationRoutes.ARG_RULE_ID) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val ruleId = backStackEntry.arguments
                ?.getString(ConfigurationRoutes.ARG_RULE_ID)
                .orEmpty()

            EditRuleScreen(
                ruleId = ruleId,
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onSaved = {
                    navController.navigate(ConfigurationRoutes.CONFIGURATION) {
                        popUpTo(ConfigurationRoutes.CONFIGURATION) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS)
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

        // H06 - Indicadores del día
        composable(route = ConfigurationRoutes.DAILY_INDICATORS) {
            DailyIndicatorsScreen(
                onBackClick = { navController.popBackStack() },
                onDemandClick = {
                    navController.navigate(ConfigurationRoutes.SPECIALTY_DEMAND)
                },
                onExportClick = {
                    navController.navigate(ConfigurationRoutes.EXPORT_REPORT)
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> { /* Indicadores: ya estamos aquí */ }
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

        // H07 - Demanda por especialidad
        composable(route = ConfigurationRoutes.SPECIALTY_DEMAND) {
            SpecialtyDemandScreen(
                onBackClick = { navController.popBackStack() },
                onExportClick = {
                    navController.navigate(ConfigurationRoutes.EXPORT_REPORT)
                },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS) {
                            popUpTo(ConfigurationRoutes.DAILY_INDICATORS) { inclusive = false }
                            launchSingleTop = true
                        }
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

        // H08 - Exportar reporte
        composable(route = ConfigurationRoutes.EXPORT_REPORT) {
            ExportReportScreen(
                onBackClick = { navController.popBackStack() },
                onBottomNavSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(ConfigurationRoutes.DAILY_INDICATORS) {
                            popUpTo(ConfigurationRoutes.DAILY_INDICATORS) { inclusive = false }
                            launchSingleTop = true
                        }
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