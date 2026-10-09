package pe.edu.upc.saludya.home.presentation.noappointments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaStateMessage

@Composable
fun NoAppointmentsScreen(
    model: NoAppointmentsUiModel = NoAppointmentsUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onReserveClick: () -> Unit = {},
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
            onButtonClick = onReserveClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun NoAppointmentsScreenPreview() {
    NoAppointmentsScreen()
}
