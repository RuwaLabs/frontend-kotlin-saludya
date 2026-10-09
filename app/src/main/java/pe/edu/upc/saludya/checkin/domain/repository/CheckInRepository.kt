package pe.edu.upc.saludya.checkin.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceTicket
import pe.edu.upc.saludya.checkin.domain.entity.HospitalConfiguration
import pe.edu.upc.saludya.checkin.domain.entity.QrCodeInfo
import pe.edu.upc.saludya.checkin.domain.entity.QueuePosition
import pe.edu.upc.saludya.checkin.domain.entity.Reservation

/**
 * Puerto del contexto "Arrival & QR Check-in". Todas las operaciones se refieren
 * al paciente autenticado; `appointmentId` es `appointments.id`.
 */
interface CheckInRepository {

    /** Citas del paciente (appointments + time_slots + doctors + specialties). */
    suspend fun getReservations(): List<Reservation>

    /** QR personal de la cita. */
    suspend fun getQrCode(appointmentId: Int): QrCodeInfo

    /**
     * Registra la llegada: inserta en `check_ins` y en `queue_entries`.
     * Es idempotente: si la cita ya tiene check-in devuelve el ticket existente.
     */
    suspend fun registerArrival(appointmentId: Int): AttendanceTicket

    /** Ticket de la cita; emite `null` mientras no exista check-in. */
    fun observeTicket(appointmentId: Int): Flow<AttendanceTicket?>

    /** Posición en la cola; emite `null` si el paciente no tiene un turno activo. */
    fun observeQueuePosition(appointmentId: Int): Flow<QueuePosition?>

    /** El paciente abandona la cola (su `queue_entries.status` pasa a ABSENT). */
    suspend fun leaveQueue(appointmentId: Int)

    /** Parámetros vigentes de `hospital_configurations`. */
    suspend fun getConfiguration(): HospitalConfiguration
}
