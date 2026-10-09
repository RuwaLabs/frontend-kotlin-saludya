package pe.edu.upc.saludya.reassignment.presentation.offerdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination

@Composable
fun OfferDetailScreen(
    offerId: Long,
    onBackClick: () -> Unit,
    onAccepted: () -> Unit,
    onRejected: () -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OfferDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(offerId) { viewModel.setOfferId(offerId) }
    LaunchedEffect(state.isAccepted) { if (state.isAccepted) onAccepted() }
    LaunchedEffect(state.isRejected) { if (state.isRejected) onRejected() }

    OfferDetailContent(
        model = OfferDetailUiModel(),
        modifier = modifier,
        onBackClick = onBackClick,
        onAcceptClick = viewModel::accept,
        onRejectClick = viewModel::reject,
        onDestinationClick = onDestinationClick,
    )
}
