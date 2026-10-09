package pe.edu.upc.saludya.features.checkin.presentation.called

import androidx.compose.runtime.Immutable
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket

/**
 * Estado inmutable de la pantalla "¡Es tu turno!".
 *
 * @property ticket ticket del paciente llamado.
 * @property postCallToleranceMinutes `hospital_configurations.post_call_tolerance_minutes`.
 */
@Immutable
data class TurnCalledUiState(
    val isLoading: Boolean = false,
    val ticket: AttendanceTicket? = null,
    val postCallToleranceMinutes: Int = 5,
    val errorMessage: String? = null,
)
