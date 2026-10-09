package pe.edu.upc.saludya.notifications.presentation.emptynotifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaStateMessage

@Composable
fun EmptyNotificationsContent(
    model: EmptyNotificationsUiModel = EmptyNotificationsUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onBackToHomeClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaStateMessage(
            title = model.heading,
            description = model.description,
            buttonText = model.buttonText,
            onButtonClick = onBackToHomeClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun EmptyNotificationsContentPreview() {
    EmptyNotificationsContent()
}
