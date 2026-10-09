package pe.edu.upc.saludya.features.patientreservations.presentation.resumenReserva

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaOutlinedButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaReservaSummaryCard
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText
import pe.edu.upc.saludya.features.patientreservations.presentation.components.toDialogLines
import pe.edu.upc.saludya.features.patientreservations.presentation.dialogs.ConfirmarCitaDialog
import pe.edu.upc.saludya.features.patientreservations.presentation.dialogs.CruceReservasDialog
import pe.edu.upc.saludya.features.patientreservations.presentation.dialogs.CupoOcupadoDialog

/** R07 · Confirma tu cita */
@Composable
fun ResumenReservaScreen(
    specialityId: Int,
    beneficiaryId: Int,
    timeSlotId: Int,
    onBooked: (appointmentId: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResumenReservaViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(specialityId, beneficiaryId, timeSlotId) {
        viewModel.start(specialityId, beneficiaryId, timeSlotId)
    }

    LaunchedEffect(state.bookedAppointmentId) {
        state.bookedAppointmentId?.let(onBooked)
    }

    when (state.dialog) {
        ReservaDialog.CONFIRMAR -> ConfirmarCitaDialog(
            summaryLines = state.summary?.toDialogLines().orEmpty(),
            onConfirm = viewModel::onConfirmBooking,
            onCancel = viewModel::onDismissDialog
        )

        ReservaDialog.CUPO_OCUPADO -> CupoOcupadoDialog(
            onViewSlots = onBack,
            onDismiss = viewModel::onDismissDialog
        )

        ReservaDialog.CRUCE -> CruceReservasDialog(
            beneficiaryName = state.beneficiaryName,
            onChooseAnotherSlot = onBack,
            onDismiss = viewModel::onDismissDialog
        )

        ReservaDialog.NONE -> Unit
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_summary_title),
        onBack = onBack,
        modifier = modifier
    ) {
        when {
            state.isLoading || state.isBooking -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_booking_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_summary)
                }
            )

            state.summary != null -> {
                SaludYaReservaSummaryCard(
                    summary = state.summary,
                    status = stringResource(R.string.reservation_status_pending)
                )
                Text(
                    text = stringResource(R.string.reservation_summary_hint),
                    style = saludYaText(14, color = SaludYaColors.Muted)
                )
                SaludYaPrimaryButton(
                    text = stringResource(R.string.reservation_confirm_booking),
                    onClick = viewModel::onConfirmClick
                )
                SaludYaOutlinedButton(
                    text = stringResource(R.string.reservation_edit),
                    onClick = onBack
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "R07 · Confirma tu cita")
@Composable
private fun ResumenReservaScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ResumenReservaScreen(
            specialityId = 0,
            beneficiaryId = 0,
            timeSlotId = 0,
            onBooked = {},
            onBack = {}
        )
    }
}
