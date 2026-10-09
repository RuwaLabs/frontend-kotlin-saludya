package pe.edu.upc.saludya.main

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.icon.calendarIcon
import pe.edu.upc.saludya.core.desingsystem.icon.homeIcon
import pe.edu.upc.saludya.core.desingsystem.icon.qrCodeIcon
import pe.edu.upc.saludya.core.desingsystem.icon.settingsIcon
import pe.edu.upc.saludya.navigation.AsistenciaDestination
import pe.edu.upc.saludya.navigation.ConfiguracionDestination
import pe.edu.upc.saludya.navigation.HomeGraph
import pe.edu.upc.saludya.features.patientreservations.presentation.navigation.PatientReservationsNavGraphRoute

enum class NavigationItem(
    val route: @Serializable Any,
    val icon: ImageVector,
    val title: Int
) {
    HOME(
        route = HomeGraph,
        icon = homeIcon,
        title = R.string.reservation_tab_home
    ),
    BOOK(
        route = PatientReservationsNavGraphRoute,
        icon = calendarIcon,
        title = R.string.reservation_tab_book
    ),
    ATTENDANCE(
        route = AsistenciaDestination,
        icon = qrCodeIcon,
        title = R.string.reservation_tab_attendance
    ),
    SETTINGS(
        route = ConfiguracionDestination,
        icon = settingsIcon,
        title = R.string.reservation_tab_settings
    )
}
