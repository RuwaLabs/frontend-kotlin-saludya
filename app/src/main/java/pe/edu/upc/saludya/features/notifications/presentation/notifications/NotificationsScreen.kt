package pe.edu.upc.saludya.features.notifications.presentation.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import pe.edu.upc.saludya.features.notifications.presentation.emptynotifications.EmptyNotificationsContent

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val reassignmentOfferLabel = stringResource(R.string.notifications_view_offer)

    if (state.notifications.isEmpty() && !state.isLoading) {
        EmptyNotificationsContent(
            onBackClick = onBackClick,
            onBackToHomeClick = onBackClick,
            onDestinationClick = onDestinationClick,
            modifier = modifier,
        )
        return
    }

    NotificationsContent(
        model = NotificationsUiModel(
            notifications = state.notifications.map {
                it.toInfoCard(reassignmentOfferLabel)
            },
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = { index ->
            state.notifications.getOrNull(index)?.let(onNotificationClick)
        },
        onDestinationClick = onDestinationClick,
    )
}
