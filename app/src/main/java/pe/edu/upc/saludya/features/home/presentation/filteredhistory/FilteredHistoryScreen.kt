package pe.edu.upc.saludya.features.home.presentation.filteredhistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.features.home.presentation.toAppointmentUiModel

@Composable
fun FilteredHistoryScreen(
    onBackClick: () -> Unit,
    onItemClick: (Long) -> Unit,
    onChangeDateClick: () -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilteredHistoryViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FilteredHistoryContent(
        model = FilteredHistoryUiModel(
            resultSummary = stringResource(
                R.string.filtered_history_result_summary,
                state.items.size
            ),
            items = state.items.map { it.toAppointmentUiModel() },
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onItemClick = { index ->
            state.items.getOrNull(index)?.let { onItemClick(it.id) }
        },
        onChangeDateClick = onChangeDateClick,
        onDestinationClick = onDestinationClick,
    )
}
