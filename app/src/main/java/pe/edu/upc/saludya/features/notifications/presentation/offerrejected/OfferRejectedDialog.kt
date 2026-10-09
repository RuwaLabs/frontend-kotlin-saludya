package pe.edu.upc.saludya.features.notifications.presentation.offerrejected

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun OfferRejectedDialog(
    model: OfferRejectedUiModel = OfferRejectedUiModel(),
    onDismissRequest: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = stringResource(R.string.offer_rejected_title),
        description = listOf(stringResource(R.string.offer_rejected_description)),
        primaryText = stringResource(R.string.offer_rejected_view),
        onPrimaryClick = onViewAppointmentClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
    )
}

@Preview(showBackground = true)
@Composable
private fun OfferRejectedDialogPreview() {
    OfferRejectedDialog()
}
