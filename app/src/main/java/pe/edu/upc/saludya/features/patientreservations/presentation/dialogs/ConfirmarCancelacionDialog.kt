package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R13 · Confirmar cancelación */
@Composable
fun ConfirmarCancelacionDialog(
    summaryLines: List<String>,
    onCancelReservation: () -> Unit,
    onKeepAppointment: () -> Unit
) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_cancel_title),
        body = summaryLines + stringResource(R.string.reservation_dialog_cancel_body),
        primaryText = stringResource(R.string.reservation_dialog_cancel_booking),
        onPrimary = onCancelReservation,
        secondaryText = stringResource(R.string.reservation_dialog_keep),
        onSecondary = onKeepAppointment,
        onDismiss = onKeepAppointment
    )
}
