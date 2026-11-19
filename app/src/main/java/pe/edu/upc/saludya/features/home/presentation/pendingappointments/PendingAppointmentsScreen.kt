package pe.edu.upc.saludya.features.home.presentation.pendingappointments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.features.home.presentation.toAppointmentUiModel

@Composable
fun PendingAppointmentsScreen(
    onBackClick: () -> Unit,
    onAppointmentClick: (Long) -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PendingAppointmentsViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { viewModel.load() }

    PendingAppointmentsContent(
        model = PendingAppointmentsUiModel(
            appointments = state.appointments.map { it.toAppointmentUiModel() },
        ),
        message = if (state.errorMessage != null) {
            stringResource(R.string.error_generic)
        } else if (state.isLoading) {
            stringResource(R.string.home_loading_appointments)
        } else if (state.appointments.isEmpty()) {
            stringResource(R.string.home_no_pending_appointments)
        } else {
            null
        },
        modifier = modifier,
        onBackClick = onBackClick,
        onAppointmentClick = { index ->
            state.appointments.getOrNull(index)?.let { onAppointmentClick(it.id) }
        },
        onDestinationClick = onDestinationClick,
    )
}
