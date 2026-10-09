package pe.edu.upc.saludya.features.home.presentation.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaHomeHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaInfoCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaPrimaryButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun HomeContent(
    model: HomeUiModel = HomeUiModel(),
    modifier: Modifier = Modifier,
    onNotificationsClick: () -> Unit = {},
    onAppointmentClick: () -> Unit = {},
    onViewAppointmentsClick: () -> Unit = {},
    onViewHistoryClick: () -> Unit = {},
    onReserveClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = {
            SaludYaHomeHeader(
                greeting = model.greeting,
                onNotificationsClick = onNotificationsClick,
            )
        },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        Text(text = model.subtitle, style = SaludYaTextStyles.Subtitle)
        Text(text = model.pendingTitle, style = SaludYaTextStyles.SectionTitle)
        AppointmentCard(model = model.nextAppointment, onClick = onAppointmentClick)
        SaludYaOutlinedButton(text = model.viewAppointmentsText, onClick = onViewAppointmentsClick)
        SaludYaInfoCard(model = model.history, onClick = onViewHistoryClick)
        SaludYaPrimaryButton(text = model.reserveText, onClick = onReserveClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun HomeContentPreview() {
    HomeContent()
}
