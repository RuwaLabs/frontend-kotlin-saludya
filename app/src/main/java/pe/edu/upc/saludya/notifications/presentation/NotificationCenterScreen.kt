package pe.edu.upc.saludya.notifications.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.notifications.domain.AppNotification
import pe.edu.upc.saludya.notifications.domain.NotificationType
import pe.edu.upc.saludya.notifications.presentation.components.NotificationCard

@Composable
fun NotificationCenterScreen(
    modifier: Modifier = Modifier,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    NotificationCenterContent(
        state = state,
        onSeeOffer = {},
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationCenterContent(
    state: NotificationUiState,
    onSeeOffer: (AppNotification) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.unreadCount > 0) {
                            "Notificaciones (${state.unreadCount})"
                        } else {
                            "Notificaciones"
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading && state.notifications.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.notifications.isEmpty() -> {
                    Text(
                        text = state.errorMessage ?: "No tienes notificaciones",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.notifications, key = { it.id }) { notification ->
                            NotificationCard(
                                notification = notification,
                                onSeeOffer = {
                                    if (notification.type == NotificationType.REASSIGNMENT_OFFER) {
                                        onSeeOffer(notification)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationCenterPreview() {
    SaludYaTheme(dynamicColor = false) {
        NotificationCenterContent(
            state = NotificationUiState(
                notifications = listOf(
                    AppNotification(
                        id = 1,
                        type = NotificationType.REASSIGNMENT_OFFER,
                        title = "Se liberó un cupo",
                        message = "Puedes adelantar tu cita. Acepta o rechaza la propuesta.",
                        createdAt = null,
                        actionId = 1
                    )
                ),
                unreadCount = 1
            ),
            onSeeOffer = {}
        )
    }
}
