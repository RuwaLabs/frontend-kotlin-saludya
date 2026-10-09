package pe.edu.upc.saludya.checkin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pe.edu.upc.saludya.checkin.presentation.called.TurnCalledScreen
import pe.edu.upc.saludya.checkin.presentation.qr.QrCodeScreen
import pe.edu.upc.saludya.checkin.presentation.queue.QueueScreen
import pe.edu.upc.saludya.checkin.presentation.ticket.TicketScreen

/** Rutas del flujo de check-in. `appointmentId` es `appointments.id`. */
object CheckInRoutes {
    const val GRAPH = "checkin"
    const val QR = "checkin/qr"

    private const val ARG = "appointmentId"
    const val TICKET = "checkin/ticket/{$ARG}"
    const val QUEUE = "checkin/queue/{$ARG}"
    const val CALLED = "checkin/called/{$ARG}"

    fun ticket(appointmentId: Int) = "checkin/ticket/$appointmentId"
    fun queue(appointmentId: Int) = "checkin/queue/$appointmentId"
    fun called(appointmentId: Int) = "checkin/called/$appointmentId"

    internal val arguments = listOf(navArgument(ARG) { type = NavType.IntType })
    internal fun appointmentId(entry: NavBackStackEntry): Int = entry.arguments?.getInt(ARG) ?: 0
}

/**
 * NavHost listo para usar con el flujo de check-in como destino inicial.
 *
 * Uso mínimo desde una Activity: `setContent { SaludYaTheme { CheckInNavHost() } }`.
 * Si la app ya tiene su propio NavHost, usar [checkInGraph] dentro de él en lugar de este.
 *
 * @param onViewReservationDetail navegación al detalle de la cita (otro bounded context).
 * @param onBottomNavItemSelected navegación entre pestañas de la barra inferior.
 */
@Composable
fun CheckInNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onViewReservationDetail: (appointmentId: Int) -> Unit = {},
    onBottomNavItemSelected: (Int) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = CheckInRoutes.GRAPH,
        modifier = modifier,
    ) {
        checkInGraph(
            navController = navController,
            onViewReservationDetail = onViewReservationDetail,
            onBottomNavItemSelected = onBottomNavItemSelected,
        )
    }
}

/**
 * Flujo completo de check-in:
 * Código QR → Ticket de atención → Mi turno → ¡Es tu turno!
 *
 * Uso: `NavHost(navController, startDestination = ...) { checkInGraph(navController, ...) }`
 *
 * @param onViewReservationDetail navegación al detalle de la cita (otro bounded context).
 * @param onBottomNavItemSelected navegación entre pestañas de la barra inferior.
 */
fun NavGraphBuilder.checkInGraph(
    navController: NavController,
    onViewReservationDetail: (appointmentId: Int) -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
) {
    /** Vuelve al ticket si ya está en la pila; si no, lo abre. */
    fun openTicket(appointmentId: Int) {
        if (!navController.popBackStack(CheckInRoutes.TICKET, inclusive = false)) {
            navController.navigate(CheckInRoutes.ticket(appointmentId))
        }
    }

    navigation(startDestination = CheckInRoutes.QR, route = CheckInRoutes.GRAPH) {

        composable(CheckInRoutes.QR) {
            QrCodeScreen(
                onBackClick = { navController.popBackStack() },
                onViewReservationDetail = onViewReservationDetail,
                onArrivalRegistered = { navController.navigate(CheckInRoutes.ticket(it)) },
                onBottomNavItemSelected = onBottomNavItemSelected,
            )
        }

        composable(CheckInRoutes.TICKET, arguments = CheckInRoutes.arguments) { entry ->
            val appointmentId = CheckInRoutes.appointmentId(entry)
            TicketScreen(
                appointmentId = appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewPosition = { navController.navigate(CheckInRoutes.queue(appointmentId)) },
                onBottomNavItemSelected = onBottomNavItemSelected,
            )
        }

        composable(CheckInRoutes.QUEUE, arguments = CheckInRoutes.arguments) { entry ->
            val appointmentId = CheckInRoutes.appointmentId(entry)
            QueueScreen(
                appointmentId = appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewTicket = { openTicket(appointmentId) },
                onTurnCalled = {
                    // "¡Es tu turno!" reemplaza a "Mi turno" en la pila.
                    navController.navigate(CheckInRoutes.called(appointmentId)) {
                        popUpTo(CheckInRoutes.QUEUE) { inclusive = true }
                    }
                },
                onLeftQueue = { navController.popBackStack(CheckInRoutes.QR, inclusive = false) },
                onBottomNavItemSelected = onBottomNavItemSelected,
            )
        }

        composable(CheckInRoutes.CALLED, arguments = CheckInRoutes.arguments) { entry ->
            val appointmentId = CheckInRoutes.appointmentId(entry)
            TurnCalledScreen(
                appointmentId = appointmentId,
                onBackClick = { navController.popBackStack() },
                onViewTicket = { openTicket(appointmentId) },
                onBottomNavItemSelected = onBottomNavItemSelected,
            )
        }
    }
}
