package pe.edu.upc.saludya.features.checkin.presentation.queue

import androidx.compose.runtime.Immutable
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.model.QueuePosition

/**
 * Estado inmutable de la pantalla "Mi turno".
 *
 * @property ticket ticket del paciente; `null` si no hay check-in.
 * @property position posición actual; `null` si el turno ya no está activo.
 * @property isQueueVisible `hospital_configurations.attendance_queue_visible`.
 * @property isLeaveDialogVisible se está pidiendo confirmación para dejar la cola.
 * @property hasLeftQueue evento: el paciente dejó la cola y la pantalla debe cerrarse.
 */
@Immutable
data class QueueUiState(
    val isLoading: Boolean = false,
    val ticket: AttendanceTicket? = null,
    val position: QueuePosition? = null,
    val isQueueVisible: Boolean = true,
    val isLeaveDialogVisible: Boolean = false,
    val hasLeftQueue: Boolean = false,
    val errorMessage: String? = null,
) {
    /** El consultorio llamó al paciente: corresponde mostrar "¡Es tu turno!". */
    val isCalled: Boolean
        get() = ticket?.isCalled == true
}
