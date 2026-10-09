package pe.edu.upc.saludya.features.checkin.presentation.queue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.checkin.application.usecase.LeaveQueueUseCase
import pe.edu.upc.saludya.features.checkin.application.usecase.ObserveQueueStatusUseCase
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeQueueStatus: ObserveQueueStatusUseCase,
    private val leaveQueue: LeaveQueueUseCase,
) : ViewModel() {

    private val appointmentId: Int = checkNotNull(savedStateHandle["appointmentId"])

    private data class LocalState(
        val isLeaveDialogVisible: Boolean = false,
        val hasLeftQueue: Boolean = false,
        val errorMessage: String? = null,
    )

    private val local = MutableStateFlow(LocalState())

    val uiState: StateFlow<QueueUiState> = combine(
        observeQueueStatus(appointmentId),
        local,
    ) { status, localState ->
        QueueUiState(
            ticket = status.ticket,
            position = status.position,
            isQueueVisible = status.isQueueVisible,
            isLeaveDialogVisible = localState.isLeaveDialogVisible,
            hasLeftQueue = localState.hasLeftQueue,
            errorMessage = localState.errorMessage,
        )
    }
        .catch { emit(QueueUiState(errorMessage = it.message ?: "No se pudo cargar tu turno.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QueueUiState(isLoading = true))

    fun onLeaveQueueClick() = local.update { it.copy(isLeaveDialogVisible = true) }

    fun onLeaveQueueDismiss() = local.update { it.copy(isLeaveDialogVisible = false) }

    fun onLeaveQueueConfirm() {
        local.update { it.copy(isLeaveDialogVisible = false, errorMessage = null) }
        viewModelScope.launch {
            try {
                leaveQueue(appointmentId)
                local.update { it.copy(hasLeftQueue = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                local.update { it.copy(errorMessage = e.message ?: "No se pudo dejar la cola.") }
            }
        }
    }
}
