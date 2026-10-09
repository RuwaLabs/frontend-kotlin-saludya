package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R16 · Modal confirmar cita */
@Composable
fun ConfirmarCitaDialog(
    summaryLines: List<String>,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_confirm_title),
        body = summaryLines + stringResource(R.string.reservation_dialog_confirm_question),
        primaryText = stringResource(R.string.reservation_dialog_confirm),
        onPrimary = onConfirm,
        secondaryText = stringResource(R.string.reservation_dialog_cancel_action),
        onSecondary = onCancel,
        onDismiss = onCancel
    )
}
