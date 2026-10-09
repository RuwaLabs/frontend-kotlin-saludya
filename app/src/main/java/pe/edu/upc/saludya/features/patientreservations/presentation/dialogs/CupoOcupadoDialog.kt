package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R11 · Cupo ocupado */
@Composable
fun CupoOcupadoDialog(onViewSlots: () -> Unit, onDismiss: () -> Unit = onViewSlots) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_slot_taken_title),
        body = listOf(stringResource(R.string.reservation_dialog_slot_taken_body)),
        primaryText = stringResource(R.string.reservation_view_slots),
        onPrimary = onViewSlots,
        onDismiss = onDismiss
    )
}
