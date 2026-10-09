package pe.edu.upc.saludya.checkin.application.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/**
 * Historia "¡Es tu turno!": observa el llamado al consultorio (`queue_entries.status`
 * = CALLED, `called_at`) junto con el plazo de tolerancia del establecimiento.
 */
class ObserveTurnCallUseCase(
    private val repository: CheckInRepository,
) {
    operator fun invoke(appointmentId: Int): Flow<TurnCall> = combine(
        repository.observeTicket(appointmentId),
        flow { emit(repository.getConfiguration()) },
    ) { ticket, configuration ->
        TurnCall(
            ticket = ticket,
            postCallToleranceMinutes = configuration.postCallToleranceMinutes,
        )
    }
}

/**
 * Resultado del caso de uso "ser llamado al consultorio".
 *
 * @property ticket ticket del paciente; `null` si no hay check-in.
 * @property postCallToleranceMinutes `hospital_configurations.post_call_tolerance_minutes`.
 */
data class TurnCall(
    val ticket: AttendanceTicket?,
    val postCallToleranceMinutes: Int,
)
