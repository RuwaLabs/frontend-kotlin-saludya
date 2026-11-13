package pe.edu.upc.saludya.features.home.presentation.appointmentdetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaInfoCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaPrimaryButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun AppointmentDetailContent(
    model: AppointmentDetailUiModel = AppointmentDetailUiModel(),
    message: String = "No se encontró esta cita.",
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRegisterArrivalClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = stringResource(R.string.appointment_detail_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        val appointment = model.appointment
        val reservation = model.reservation
        if (appointment == null || reservation == null) {
            Text(message)
            return@SaludYaScreenLayout
        }
        AppointmentCard(model = appointment)
        SaludYaInfoCard(
            model = InfoCardUiModel(
                title = stringResource(R.string.appointment_detail_reservation_title),
                lines = reservation.lines,
            )
        )
        SaludYaPrimaryButton(text = "Ver código de reserva", onClick = onRegisterArrivalClick)
        if (appointment.status == "RESERVED" || appointment.status == "CONFIRMED" ||
            appointment.status == "Reservada" || appointment.status == "Confirmada") {
            SaludYaOutlinedButton(text = stringResource(R.string.appointment_detail_cancel), onClick = onCancelClick)
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                style = SaludYaTextStyles.Caption,
            )
        }
        Text(text = stringResource(R.string.appointment_detail_note), style = SaludYaTextStyles.Caption)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun AppointmentDetailContentPreview() {
    AppointmentDetailContent()
}
