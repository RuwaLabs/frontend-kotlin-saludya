package pe.edu.upc.saludya.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.notifications.application.AcceptReassignmentNotificationUseCase
import pe.edu.upc.saludya.notifications.application.DeleteNotificationUseCase
import pe.edu.upc.saludya.notifications.application.GetNotificationsUseCase
import pe.edu.upc.saludya.notifications.application.MarkNotificationReadUseCase
import pe.edu.upc.saludya.notifications.application.RejectReassignmentNotificationUseCase
import pe.edu.upc.saludya.notifications.application.SyncReassignmentNotificationsUseCase
import pe.edu.upc.saludya.notifications.domain.AppNotification
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotifications: GetNotificationsUseCase,
    private val syncReassignmentNotifications: SyncReassignmentNotificationsUseCase,
    private val acceptReassignmentNotification: AcceptReassignmentNotificationUseCase,
    private val rejectReassignmentNotification: RejectReassignmentNotificationUseCase,
    private val markNotificationRead: MarkNotificationReadUseCase,
    private val deleteNotification: DeleteNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    /**
     * Syncs the reassignment offers into the inbox and reads the inbox back into the UI state.
     * Called on init and after every action so the screen reflects the latest data.
     */
    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                syncReassignmentNotifications()
                val notifications = getNotifications()
                _uiState.update {
                    it.copy(
                        notifications = notifications,
                        unreadCount = notifications.count { notification -> !notification.isRead },
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "No se pudo cargar")
                }
            }
        }
    }

    fun onMarkRead(notification: AppNotification) {
        viewModelScope.launch {
            markNotificationRead(notification.id)
            load()
        }
    }

    fun onAccept(notification: AppNotification) {
        viewModelScope.launch {
            acceptReassignmentNotification(notification)
            load()
        }
    }

    fun onReject(notification: AppNotification) {
        viewModelScope.launch {
            rejectReassignmentNotification(notification)
            load()
        }
    }

    fun onDismiss(notification: AppNotification) {
        viewModelScope.launch {
            deleteNotification(notification.id)
            load()
        }
    }
}
