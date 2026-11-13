package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R14 · Cancelación fuera de plazo */
@Composable
fun CancelacionFueraPlazoDialog(onBackToDetail: () -> Unit, onDismiss: () -> Unit = onBackToDetail) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_deadline_title),
        body = listOf(stringResource(R.string.reservation_dialog_deadline_body)),
        primaryText = stringResource(R.string.reservation_dialog_back_to_detail),
        onPrimary = onBackToDetail,
        onDismiss = onDismiss
    )
}
