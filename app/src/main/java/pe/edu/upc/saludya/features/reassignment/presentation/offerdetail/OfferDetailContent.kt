package pe.edu.upc.saludya.features.reassignment.presentation.offerdetail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
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
        header = { SaludYaBackHeader(title = stringResource(R.string.offer_detail_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaInfoCard(
            model = InfoCardUiModel(
                title = stringResource(R.string.offer_detail_current_title),
                lines = model.currentSchedule.lines,
            )
        )
        SaludYaInfoCard(
            model = InfoCardUiModel(
                title = stringResource(R.string.offer_detail_offered_title),
                lines = model.offeredSchedule.lines,
                footer = model.offeredSchedule.footer,
            )
        )
        Text(text = stringResource(R.string.offer_detail_note), style = SaludYaTextStyles.Subtitle)
        SaludYaPrimaryButton(text = stringResource(R.string.offer_detail_accept), onClick = onAcceptClick)
        SaludYaOutlinedButton(text = stringResource(R.string.offer_detail_reject), onClick = onRejectClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun OfferDetailContentPreview() {
    OfferDetailContent()
}
