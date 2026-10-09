package pe.edu.upc.saludya.checkin.presentation.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.checkin.application.usecase.LeaveQueueUseCase
import pe.edu.upc.saludya.checkin.application.usecase.ObserveQueueStatusUseCase
import pe.edu.upc.saludya.checkin.infrastructure.di.CheckInModule

/**
 * ViewModel de "Mi turno": expone el estado de la cola del paciente y gestiona
 * la acción "Dejar la cola".
 */
class QueueViewModel(
    private val appointmentId: Int,
    observeQueueStatus: ObserveQueueStatusUseCase,
    private val leaveQueue: LeaveQueueUseCase,
) : ViewModel() {

    /** Estado propio de la pantalla (no proviene del repositorio). */
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

    /** Confirma la salida: el turno pasa a ABSENT en `queue_entries`. */
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

    companion object {
        fun factory(appointmentId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                QueueViewModel(appointmentId, CheckInModule.observeQueueStatus, CheckInModule.leaveQueue)
            }
        }
    }
}
