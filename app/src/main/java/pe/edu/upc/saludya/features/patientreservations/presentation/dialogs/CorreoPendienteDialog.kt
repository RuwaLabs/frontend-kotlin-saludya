package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

/** R15 · Correo pendiente */
@Composable
fun CorreoPendienteDialog(onViewReservation: () -> Unit, onDismiss: () -> Unit = onViewReservation) {
    SaludYaDialog(
        title = stringResource(R.string.reservation_dialog_email_title),
        body = listOf(stringResource(R.string.reservation_dialog_email_body)),
        primaryText = stringResource(R.string.reservation_dialog_view_reservation),
        onPrimary = onViewReservation,
        onDismiss = onDismiss
    )
}
