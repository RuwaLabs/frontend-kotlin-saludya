package pe.edu.upc.saludya.home.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.home.presentation.toAppointmentUiModel

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryContent(
        model = HistoryUiModel(
            items = state.items.map { it.toAppointmentUiModel() },
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onFilterClick = onFilterClick,
        onItemClick = { index ->
            state.items.getOrNull(index)?.let { onItemClick(it.id) }
        },
        onDestinationClick = onDestinationClick,
    )
}
