package pe.edu.upc.saludya.features.home.presentation.appointmentdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.features.home.presentation.toAppointmentUiModel

@Composable
fun AppointmentDetailScreen(
    appointmentId: Long,
    onBackClick: () -> Unit,
    onRegisterArrivalClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppointmentDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var confirmCancel by remember { mutableStateOf(false) }
    LaunchedEffect(appointmentId) { viewModel.setAppointmentId(appointmentId) }
    LaunchedEffect(state.isCancelled) { if (state.isCancelled) onCancelClick() }

    val appointment = state.appointment

    AppointmentDetailContent(
        model = AppointmentDetailUiModel(
            appointment = appointment?.toAppointmentUiModel(),
            reservation = appointment?.let {
                InfoCardUiModel(
                    title = stringResource(R.string.appointment_detail_reservation_title),
                    lines = listOf(
                        it.bookingCode,
                        "Beneficiario: ${it.patientName}",
                        stringResource(R.string.appointment_detail_reservation_note),
                    ),
                )
            },
        ),
        message = state.errorMessage ?: if (state.isLoading) "Cargando cita…" else "No se encontró esta cita.",
        errorMessage = state.errorMessage,
        modifier = modifier,
        onBackClick = onBackClick,
        onRegisterArrivalClick = onRegisterArrivalClick,
        onCancelClick = { confirmCancel = true },
        onDestinationClick = onDestinationClick,
    )

    if (confirmCancel) {
        AlertDialog(
            onDismissRequest = { confirmCancel = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Confirmas que deseas cancelar esta reserva?") },
            confirmButton = {
                TextButton(onClick = { confirmCancel = false; viewModel.cancel() }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmCancel = false }) { Text("Conservar cita") }
            },
        )
    }
}
