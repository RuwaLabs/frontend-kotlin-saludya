package pe.edu.upc.saludya.notifications.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationType

@Composable
fun NotificationCard(
    notification: AppNotification,
    onSeeOffer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isActionable = notification.type == NotificationType.REASSIGNMENT_OFFER &&
        notification.actionId != null

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = notification.message, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isActionable) {
                    TextButton(onClick = onSeeOffer) { Text("Ver oferta") }
                } else {
                    Text(
                        text = "Hace 15 min",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNotificationCard() {
    NotificationCard(
        notification = AppNotification(
            id = 1,
            type = NotificationType.PATIENT_CALLED,
            title = "Horario más temprano disponible",
            message = "Medicina general · 6 oct\nOferta recibida a las 08:00",
            createdAt = null,
            actionId = 1
        ),
        onSeeOffer = {}
    )
}
