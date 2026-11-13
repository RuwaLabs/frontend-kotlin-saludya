package pe.edu.upc.saludya.features.reassignment.presentation.offerexpired

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun OfferExpiredDialog(
    model: OfferExpiredUiModel = OfferExpiredUiModel(),
    onDismissRequest: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = stringResource(R.string.offer_expired_title),
        description = listOf(stringResource(R.string.offer_expired_description)),
        primaryText = stringResource(R.string.offer_expired_primary),
        onPrimaryClick = onViewAppointmentClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
    )
}

@Preview(showBackground = true)
@Composable
private fun OfferExpiredDialogPreview() {
    OfferExpiredDialog()
}
