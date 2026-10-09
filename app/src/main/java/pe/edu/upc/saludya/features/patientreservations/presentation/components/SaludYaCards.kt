package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.core.desingsystem.icon.health_cross

@Composable
fun SaludYaInfoCard(
    title: String,
    modifier: Modifier = Modifier,
    body: List<String> = emptyList(),
    status: String? = null,
    statusColor: Color = SaludYaColors.Primary,
    containerColor: Color = SaludYaColors.White,
    onClick: (() -> Unit)? = null
) {
    val content: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = saludYaText(16, FontWeight.SemiBold))
            if (body.isNotEmpty()) {
                Text(
                    text = body.joinToString("\n"),
                    style = saludYaText(13, color = SaludYaColors.Muted)
                )
            }
            if (status != null) {
                Text(text = status, style = saludYaText(12, FontWeight.SemiBold, statusColor))
            }
        }
    }

    val shape = RoundedCornerShape(16.dp)
    val colors = CardDefaults.cardColors(containerColor = containerColor)
    val border = BorderStroke(1.dp, SaludYaColors.Border)

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(0.dp)
        ) { content() }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            colors = colors,
            border = border,
            elevation = CardDefaults.cardElevation(0.dp)
        ) { content() }
    }
}

@Composable
fun SaludYaStateMessage(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            imageVector = health_cross,
            contentDescription = null,
            tint = SaludYaColors.Primary,
            modifier = Modifier.size(48.dp)
        )
        Text(text = title, style = saludYaText(23, FontWeight.SemiBold))
        Text(text = description, style = saludYaText(15, color = SaludYaColors.Muted))
    }
}
