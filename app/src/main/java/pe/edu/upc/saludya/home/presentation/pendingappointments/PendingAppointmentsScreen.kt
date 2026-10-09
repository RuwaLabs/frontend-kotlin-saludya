package pe.edu.upc.saludya.home.presentation.pendingappointments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.home.presentation.toAppointmentUiModel

@Composable
fun PendingAppointmentsScreen(
    onBackClick: () -> Unit,
    onAppointmentClick: (Long) -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PendingAppointmentsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    PendingAppointmentsContent(
        model = PendingAppointmentsUiModel(
            appointments = state.appointments.map { it.toAppointmentUiModel() },
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onAppointmentClick = { index ->
            state.appointments.getOrNull(index)?.let { onAppointmentClick(it.id) }
        },
        onDestinationClick = onDestinationClick,
    )
}
