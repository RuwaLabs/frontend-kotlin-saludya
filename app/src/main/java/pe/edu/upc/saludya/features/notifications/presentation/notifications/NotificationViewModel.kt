package pe.edu.upc.saludya.features.notifications.presentation.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.notifications.application.DeleteNotificationUseCase
import pe.edu.upc.saludya.features.notifications.application.GetNotificationsUseCase
import pe.edu.upc.saludya.features.notifications.application.SyncReassignmentNotificationsUseCase
import pe.edu.upc.saludya.features.notifications.domain.AppNotification
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotifications: GetNotificationsUseCase,
    private val syncReassignmentNotifications: SyncReassignmentNotificationsUseCase,
    private val deleteNotification: DeleteNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                syncReassignmentNotifications()
                val notifications = getNotifications()
                _uiState.update { it.copy(notifications = notifications, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "No se pudo cargar")
                }
            }
        }
    }

    fun onDismiss(notification: AppNotification) {
        viewModelScope.launch {
            deleteNotification(notification.id)
            load()
        }
    }
}
