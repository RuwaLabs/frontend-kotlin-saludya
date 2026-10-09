package pe.edu.upc.saludya.notifications.presentation.offeraccepted

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaPrimaryButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun OfferAcceptedScreen(
    model: OfferAcceptedUiModel = OfferAcceptedUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        Text(text = model.headline, style = SaludYaTextStyles.SuccessHeadline)
        AppointmentCard(model = model.appointment)
        Text(text = model.note, style = SaludYaTextStyles.Subtitle)
        SaludYaPrimaryButton(text = model.viewAppointmentText, onClick = onViewAppointmentClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun OfferAcceptedScreenPreview() {
    OfferAcceptedScreen()
}
