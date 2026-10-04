package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.core.desingsystem.icon.appointmentIcon
import pe.edu.upc.saludya.core.desingsystem.icon.calendarIcon
import pe.edu.upc.saludya.core.desingsystem.icon.homeIcon
import pe.edu.upc.saludya.core.desingsystem.icon.sunIcon
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

enum class SaludYaBottomNavItem(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Inicio", homeIcon),
    CALENDAR("Calendario", calendarIcon),
    APPOINTMENT("Cita", appointmentIcon),
    CONFIGURATION("Configuración", sunIcon),
}

@Composable
fun BottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SaludYaBottomNavItem.entries.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surface
                    )
                    .clickable { onItemSelected(index) }
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp),
                )
                Box(modifier = Modifier.height(4.dp))
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar - Inicio")
@Composable
private fun BottomNavHomePreview() {
    SaludYaTheme(dynamicColor = false) {
        BottomNavigationBar(selectedIndex = 0, onItemSelected = {})
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar - Calendario")
@Composable
private fun BottomNavCalendarPreview() {
    SaludYaTheme(dynamicColor = false) {
        BottomNavigationBar(selectedIndex = 1, onItemSelected = {})
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar - Cita")
@Composable
private fun BottomNavAppointmentPreview() {
    SaludYaTheme(dynamicColor = false) {
        BottomNavigationBar(selectedIndex = 2, onItemSelected = {})
    }
}

@Preview(showBackground = true, name = "BottomNavigationBar - Configuración")
@Composable
private fun BottomNavConfigurationPreview() {
    SaludYaTheme(dynamicColor = false) {
        BottomNavigationBar(selectedIndex = 3, onItemSelected = {})
    }
}