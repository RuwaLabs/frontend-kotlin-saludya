package pe.edu.upc.saludya.notifications.presentation.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.presentation.emptynotifications.EmptyNotificationsContent

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNotificationClick: (AppNotification) -> Unit,
    onDestinationClick: (SaludYaDestination) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
            notifications = state.notifications.map { it.toInfoCard() },
        ),
        modifier = modifier,
        onBackClick = onBackClick,
        onNotificationClick = { index ->
            state.notifications.getOrNull(index)?.let(onNotificationClick)
        },
        onDestinationClick = onDestinationClick,
    )
}
