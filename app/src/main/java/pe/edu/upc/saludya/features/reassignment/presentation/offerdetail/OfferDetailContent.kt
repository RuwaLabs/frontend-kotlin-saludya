package pe.edu.upc.saludya.features.reassignment.presentation.offerdetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaInfoCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaPrimaryButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun OfferDetailContent(
    model: OfferDetailUiModel = OfferDetailUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaInfoCard(model = model.currentSchedule)
        SaludYaInfoCard(model = model.offeredSchedule)
        Text(text = model.note, style = SaludYaTextStyles.Subtitle)
        SaludYaPrimaryButton(text = model.acceptText, onClick = onAcceptClick)
        SaludYaOutlinedButton(text = model.rejectText, onClick = onRejectClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun OfferDetailContentPreview() {
    OfferDetailContent()
}
