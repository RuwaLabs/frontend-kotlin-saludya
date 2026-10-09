package pe.edu.upc.saludya.notifications.presentation.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaInfoCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout

@Composable
fun NotificationsContent(
    model: NotificationsUiModel = NotificationsUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onNotificationClick: (index: Int) -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        model.notifications.forEachIndexed { index, notification ->
            SaludYaInfoCard(model =  notification, onClick = { onNotificationClick(index) })
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun NotificationsContentPreview() {
    NotificationsContent()
}
