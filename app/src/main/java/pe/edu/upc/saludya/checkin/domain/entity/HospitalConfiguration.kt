package pe.edu.upc.saludya.checkin.domain.entity

/**
 * Parámetros de `hospital_configurations` que usa el contexto de check-in.
 * Los valores por defecto son los `DEFAULT` del esquema.
 *
 * @property checkInToleranceMinutes `check_in_tolerance_minutes`.
 * @property postCallToleranceMinutes `post_call_tolerance_minutes`: plazo para acercarse
 *   al consultorio después del llamado.
 * @property attendanceQueueVisible `attendance_queue_visible`: si el paciente puede ver
 *   su posición en la cola.
 */
data class HospitalConfiguration(
    val checkInToleranceMinutes: Int = 15,
    val postCallToleranceMinutes: Int = 5,
    val attendanceQueueVisible: Boolean = true,
)
