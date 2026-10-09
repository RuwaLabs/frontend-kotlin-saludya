package pe.edu.upc.saludya.features.home.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.features.home.presentation.toAppointmentUiModel

@Composable
fun HomeScreen(
    onNotificationsClick: () -> Unit,
    onAppointmentClick: (Long) -> Unit,
    onViewAppointmentsClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onReserveClick: () -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val fallback = HomeUiModel()
    val appointment = state.nextAppointment

    HomeContent(
        model = HomeUiModel(
            nextAppointment = appointment?.toAppointmentUiModel() ?: fallback.nextAppointment,
        ),
        modifier = modifier,
        onNotificationsClick = onNotificationsClick,
        onAppointmentClick = { appointment?.let { onAppointmentClick(it.id) } },
        onViewAppointmentsClick = onViewAppointmentsClick,
        onViewHistoryClick = onViewHistoryClick,
        onReserveClick = onReserveClick,
        onDestinationClick = onDestinationClick,
    )
}
