package pe.edu.upc.saludya.home.presentation.appointmentdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.InfoCardUiModel
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.home.presentation.toAppointmentUiModel

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
    LaunchedEffect(appointmentId) { viewModel.setAppointmentId(appointmentId) }

    val fallback = AppointmentDetailUiModel()
    val appointment = state.appointment

    AppointmentDetailContent(
        model = AppointmentDetailUiModel(
            appointment = appointment?.toAppointmentUiModel() ?: fallback.appointment,
            reservation = appointment?.let {
                InfoCardUiModel(
                    title = "Código de reserva",
                    lines = listOf(
                        it.bookingCode,
                        "Beneficiario: ${it.patientName}",
                        "Comprobante enviado a tu correo.",
                    ),
                )
            } ?: fallback.reservation,
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onRegisterArrivalClick = onRegisterArrivalClick,
        onCancelClick = onCancelClick,
        onDestinationClick = onDestinationClick,
    )
}
