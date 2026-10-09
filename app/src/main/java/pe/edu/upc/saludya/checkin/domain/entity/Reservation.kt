package pe.edu.upc.saludya.checkin.domain.entity

import java.time.LocalDate
import java.time.LocalTime

/** Estados de `appointments.status`. */
enum class AppointmentStatus { RESERVED, CONFIRMED, CANCELLED, ABSENT, ATTENDED, EXPIRED }

/**
 * Reserva vista desde el contexto de check-in.
 *
 * Es una proyección de `appointments` unida a `time_slots`, `doctors`, `specialties`
 * y `patients`; el contexto de check-in no modifica estas tablas, solo las lee.
 *
 * @property id `appointments.id`.
 * @property timeSlotId `appointments.id_time_slot`.
 * @property bookingOrder `appointments.booking_order` (orden de la cola de reserva).
 * @property status `appointments.status`.
 * @property patientFullName `patients.name` + `patients.lastname`.
 * @property specialty `specialties.name`.
 * @property doctorFullName `doctors.name` + `doctors.lastname`.
 * @property date `time_slots.date`.
 * @property startHour `time_slots.start_hour`.
 * @property endHour `time_slots.end_hour`.
 * @property consultingRoom número de consultorio. **No existe en el esquema actual**; es `null`
 *   hasta que se agregue la columna y la UI lo oculta en ese caso.
 * @property waitingRoom sala de espera. **No existe en el esquema actual** (ídem).
 */
data class Reservation(
    val id: Int,
    val timeSlotId: Int,
    val bookingOrder: Int,
    val status: AppointmentStatus,
    val patientFullName: String,
    val specialty: String,
    val doctorFullName: String,
    val date: LocalDate,
    val startHour: LocalTime,
    val endHour: LocalTime,
    val consultingRoom: String? = null,
    val waitingRoom: String? = null,
) {
    /** Solo las reservas vigentes pueden hacer check-in. */
    val canCheckIn: Boolean
        get() = status == AppointmentStatus.RESERVED || status == AppointmentStatus.CONFIRMED
}
