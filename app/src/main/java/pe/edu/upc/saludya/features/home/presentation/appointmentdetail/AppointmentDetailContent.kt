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
        AppointmentCard(model = model.appointment)
        SaludYaInfoCard(
            model = InfoCardUiModel(
                title = stringResource(R.string.appointment_detail_reservation_title),
                lines = model.reservation.lines,
            )
        )
        SaludYaPrimaryButton(text = stringResource(R.string.appointment_detail_register_arrival), onClick = onRegisterArrivalClick)
        SaludYaOutlinedButton(text = stringResource(R.string.appointment_detail_cancel), onClick = onCancelClick)
        Text(text = stringResource(R.string.appointment_detail_note), style = SaludYaTextStyles.Caption)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun AppointmentDetailContentPreview() {
    AppointmentDetailContent()
}
