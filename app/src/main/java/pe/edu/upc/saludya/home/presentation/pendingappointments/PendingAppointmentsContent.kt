package pe.edu.upc.saludya.home.presentation.pendingappointments

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun PendingAppointmentsContent(
    model: PendingAppointmentsUiModel = PendingAppointmentsUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAppointmentClick: (index: Int) -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        model.appointments.forEachIndexed { index, appointment ->
            AppointmentCard(model = appointment, onClick = { onAppointmentClick(index) })
        }
        Text(text = model.hint, style = SaludYaTextStyles.Hint)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun PendingAppointmentsContentPreview() {
    PendingAppointmentsContent()
}
