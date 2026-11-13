package pe.edu.upc.saludya.features.notifications.presentation.offeraccepted

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
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
        header = { SaludYaBackHeader(title = stringResource(R.string.offer_accepted_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        Text(text = stringResource(R.string.offer_accepted_headline), style = SaludYaTextStyles.SuccessHeadline)
        AppointmentCard(model = model.appointment)
        Text(text = stringResource(R.string.offer_accepted_note), style = SaludYaTextStyles.Subtitle)
        SaludYaPrimaryButton(text = stringResource(R.string.offer_accepted_view), onClick = onViewAppointmentClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun OfferAcceptedScreenPreview() {
    OfferAcceptedScreen()
}
