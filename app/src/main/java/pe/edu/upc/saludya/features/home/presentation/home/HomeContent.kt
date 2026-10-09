package pe.edu.upc.saludya.features.home.presentation.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
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
                greeting = stringResource(R.string.home_greeting),
                onNotificationsClick = onNotificationsClick,
            )
        },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        Text(text = stringResource(R.string.home_subtitle), style = SaludYaTextStyles.Subtitle)
        Text(text = stringResource(R.string.home_pending_title), style = SaludYaTextStyles.SectionTitle)
        AppointmentCard(model = model.nextAppointment, onClick = onAppointmentClick)
        SaludYaOutlinedButton(text = stringResource(R.string.home_view_appointments), onClick = onViewAppointmentsClick)
        SaludYaInfoCard(
            model = InfoCardUiModel(
                title = stringResource(R.string.home_history_title),
                lines = listOf(stringResource(R.string.home_history_body)),
                footer = stringResource(R.string.home_history_footer),
            ),
            onClick = onViewHistoryClick,
        )
        SaludYaPrimaryButton(text = stringResource(R.string.home_reserve), onClick = onReserveClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun HomeContentPreview() {
    HomeContent()
}
