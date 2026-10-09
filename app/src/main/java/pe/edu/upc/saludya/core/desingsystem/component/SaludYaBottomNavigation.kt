package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class SaludYaDestination(val label: String, val icon: ImageVector) {
    Inicio("Inicio", SaludYaIcons.Home),
    ReservarCita("Reservar cita", SaludYaIcons.Calendar),
    Asistencia("Asistencia", SaludYaIcons.Qr),
    Configuracion("Configuración", SaludYaIcons.Settings),
}

@Composable
fun SaludYaBottomNavigation(
    selected: SaludYaDestination,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SaludYaColors.Surface)
            .navigationBarsPadding()
            .height(72.dp),
        verticalAlignment = Alignment.Top,
    ) {
        SaludYaDestination.entries.forEach { destination ->
            val isSelected = destination == selected
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isSelected) SaludYaColors.Soft else Color.Transparent)
                    .clickable(role = Role.Tab) { onDestinationClick(destination) }
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = destination.icon,
                    contentDescription = null,
                    tint = if (isSelected) SaludYaColors.Primary else SaludYaColors.Muted,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = destination.label,
                    style = if (isSelected) SaludYaTextStyles.NavLabelSelected else SaludYaTextStyles.NavLabel,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
