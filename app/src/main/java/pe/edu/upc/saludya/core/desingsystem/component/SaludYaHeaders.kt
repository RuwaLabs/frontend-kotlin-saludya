package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SaludYaBackHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = SaludYaIcons.Back,
            contentDescription = null,
            tint = SaludYaColors.Primary,
            modifier = Modifier
                .size(24.dp)
                .clickable(role = Role.Button, onClick = onBackClick),
        )
        Text(
            text = title,
            style = SaludYaTextStyles.ScreenTitle,
            modifier = Modifier.width(260.dp),
        )
    }
}

@Composable
fun SaludYaHomeHeader(
    greeting: String,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = greeting,
            style = SaludYaTextStyles.ScreenTitle,
            modifier = Modifier.width(260.dp),
        )
        Icon(
            imageVector = SaludYaIcons.Bell,
            contentDescription = null,
            tint = SaludYaColors.Primary,
            modifier = Modifier
                .size(24.dp)
                .clickable(role = Role.Button, onClick = onNotificationsClick),
        )
    }
}
