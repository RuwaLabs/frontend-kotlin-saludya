package pe.edu.upc.saludya.home.presentation.appointmentdetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
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
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        AppointmentCard(model = model.appointment)
        SaludYaInfoCard(model = model.reservation)
        SaludYaPrimaryButton(text = model.registerArrivalText, onClick = onRegisterArrivalClick)
        SaludYaOutlinedButton(text = model.cancelText, onClick = onCancelClick)
        Text(text = model.note, style = SaludYaTextStyles.Caption)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun AppointmentDetailContentPreview() {
    AppointmentDetailContent()
}
