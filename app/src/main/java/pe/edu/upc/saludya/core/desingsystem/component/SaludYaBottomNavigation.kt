package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.ui.graphics.vector.ImageVector

enum class SaludYaDestination(val label: String, val icon: ImageVector) {
    Inicio("Inicio", SaludYaIcons.Home),
    ReservarCita("Reservar cita", SaludYaIcons.Calendar),
    Asistencia("Asistencia", SaludYaIcons.Qr),
    Configuracion("Configuración", SaludYaIcons.Settings),
}