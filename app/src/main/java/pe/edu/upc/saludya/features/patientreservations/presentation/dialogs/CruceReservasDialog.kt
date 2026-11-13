package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R12 · Cruce de reservas */
@Composable
fun CruceReservasDialog(
    beneficiaryName: String,
    onChooseAnotherSlot: () -> Unit,
    onDismiss: () -> Unit = onChooseAnotherSlot
) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_overlap_title),
        body = listOf(
            stringResource(R.string.reservation_dialog_overlap_body, beneficiaryName)
        ),
        primaryText = stringResource(R.string.reservation_dialog_choose_another_slot),
        onPrimary = onChooseAnotherSlot,
        onDismiss = onDismiss
    )
}
