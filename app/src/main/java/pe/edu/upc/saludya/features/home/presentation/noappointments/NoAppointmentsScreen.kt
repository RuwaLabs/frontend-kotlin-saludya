package pe.edu.upc.saludya.features.home.presentation.noappointments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
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
        header = { SaludYaBackHeader(title = stringResource(R.string.no_appointments_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaStateMessage(
            title = stringResource(R.string.no_appointments_heading),
            description = stringResource(R.string.no_appointments_description),
            buttonText = stringResource(R.string.no_appointments_button),
            onButtonClick = onReserveClick,
        )
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun NoAppointmentsScreenPreview() {
    NoAppointmentsScreen()
}
