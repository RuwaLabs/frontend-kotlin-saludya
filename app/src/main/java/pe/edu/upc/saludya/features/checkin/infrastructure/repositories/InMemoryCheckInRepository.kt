package pe.edu.upc.saludya.features.checkin.infrastructure.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import pe.edu.upc.saludya.features.checkin.domain.model.CheckInException
import pe.edu.upc.saludya.features.checkin.domain.model.AppointmentStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceQueue
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceQueueStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.model.CheckIn
import pe.edu.upc.saludya.features.checkin.domain.model.CheckInStatus
import pe.edu.upc.saludya.features.checkin.domain.model.HospitalConfiguration
import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import pe.edu.upc.saludya.features.checkin.domain.model.QueueEntry
import pe.edu.upc.saludya.features.checkin.domain.model.QueueEntryStatus
import pe.edu.upc.saludya.features.checkin.domain.model.QueuePosition
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import pe.edu.upc.saludya.features.checkin.domain.model.buildTicketCode
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Implementación en memoria con datos de muestra que replican las tablas del esquema
 * (`appointments`, `check_ins`, `attendance_queues`, `queue_entries`,
 * `hospital_configurations`). Es un adaptador de la capa de infraestructura: sustituirlo por
 * uno remoto (Retrofit) que implemente [CheckInRepository] no afecta a las demás capas.
 *
 * @param now reloj inyectable para pruebas.
 */
class InMemoryCheckInRepository(
    private val now: () -> LocalDateTime = { LocalDateTime.now() },
) : CheckInRepository {

    /** Filas mutables: `check_ins` y `queue_entries`. */
    private data class Tables(
        val checkIns: List<CheckIn> = emptyList(),
        val entries: List<QueueEntry> = emptyList(),
    )

    private val configuration = HospitalConfiguration()

    private val reservations = listOf(
        Reservation(
            id = 142, timeSlotId = 31, bookingOrder = 3, status = AppointmentStatus.CONFIRMED,
            patientFullName = "Lucía Torres", specialty = "Medicina general",
            doctorFullName = "Ana Ríos", date = LocalDate.of(2026, 10, 6),
            startHour = LocalTime.of(9, 0), endHour = LocalTime.of(9, 30),
            consultingRoom = "02", waitingRoom = "A",
        ),
        Reservation(
            id = 157, timeSlotId = 48, bookingOrder = 1, status = AppointmentStatus.RESERVED,
            patientFullName = "Lucía Torres", specialty = "Pediatría",
            doctorFullName = "Carlos Vega", date = LocalDate.of(2026, 10, 12),
            startHour = LocalTime.of(11, 30), endHour = LocalTime.of(12, 0),
            consultingRoom = "05", waitingRoom = "B",
        ),
        Reservation(
            id = 163, timeSlotId = 55, bookingOrder = 2, status = AppointmentStatus.RESERVED,
            patientFullName = "Lucía Torres", specialty = "Cardiología",
            doctorFullName = "María Salas", date = LocalDate.of(2026, 10, 20),
            startHour = LocalTime.of(16, 0), endHour = LocalTime.of(16, 30),
            consultingRoom = "08", waitingRoom = "C",
        ),
    )

    /** Una cola por bloque horario (UNIQUE id_time_slot + date). */
    private val queues = reservations.mapIndexed { index, reservation ->
        AttendanceQueue(id = DEMO_QUEUE_ID + index, timeSlotId = reservation.timeSlotId, date = reservation.date)
    }

    /** La cola de muestra arranca con dos pacientes que llegaron antes. */
    private val tables = MutableStateFlow(
        Tables()
            .withArrival(140, DEMO_QUEUE_ID, qrToken(DEMO_DATE, 140), DEMO_DATE.atTime(8, 40))
            .withArrival(141, DEMO_QUEUE_ID, qrToken(DEMO_DATE, 141), DEMO_DATE.atTime(8, 48)),
    )

    override suspend fun getReservations(): List<Reservation> = reservations

    override suspend fun getQrCode(appointmentId: Int): QrCodeInfo {
        val reservation = reservationOf(appointmentId)
        return QrCodeInfo(token = qrToken(reservation.date, reservation.id), appointmentId = reservation.id)
    }

    override suspend fun registerArrival(appointmentId: Int): AttendanceTicket {
        val reservation = reservationOf(appointmentId)
        if (!reservation.canCheckIn) throw CheckInException.ReservationNotEligible()
        val queue = queueOf(reservation)
        if (queue.status != AttendanceQueueStatus.OPEN) throw CheckInException.QueueNotOpen()

        tables.update { current ->
            // check_ins.id_appointment es UNIQUE: un solo check-in por cita.
            if (current.checkIns.any { it.appointmentId == appointmentId }) return@update current
            val registered = current.withArrival(
                appointmentId, queue.id, qrToken(reservation.date, appointmentId), now(),
            )
            // Solo demo: simula cinco llegadas posteriores para reproducir el "3 de 8" del diseño.
            if (queue.id == DEMO_QUEUE_ID) {
                (143..147).fold(registered) { acc, other ->
                    acc.withArrival(other, queue.id, qrToken(reservation.date, other), now())
                }
            } else {
                registered
            }
        }
        return checkNotNull(ticketOf(tables.value, appointmentId))
    }

    override fun observeTicket(appointmentId: Int): Flow<AttendanceTicket?> =
        tables.map { ticketOf(it, appointmentId) }.distinctUntilChanged()

    override fun observeQueuePosition(appointmentId: Int): Flow<QueuePosition?> =
        tables.map { positionOf(it, appointmentId) }

    override suspend fun leaveQueue(appointmentId: Int) {
        tables.update { current ->
            val mine = entryOf(current, appointmentId) ?: return@update current
            current.copy(
                entries = current.entries.map {
                    if (it.id == mine.id) it.copy(status = QueueEntryStatus.ABSENT) else it
                },
            )
        }
    }

    override suspend fun getConfiguration(): HospitalConfiguration = configuration

    /**
     * Solo demo/pruebas: el consultorio llama al siguiente turno de la cola de la cita.
     * El turno que estaba llamado pasa a ATTENDED y el primero en espera pasa a CALLED.
     */
    fun callNext(appointmentId: Int) {
        val queue = queueOf(reservationOf(appointmentId))
        tables.update { current ->
            val next = current.entries
                .filter { it.attendanceQueueId == queue.id && it.status == QueueEntryStatus.WAITING }
                .minByOrNull { it.position }
            current.copy(
                entries = current.entries.map { entry ->
                    when {
                        entry.attendanceQueueId != queue.id -> entry
                        entry.status == QueueEntryStatus.CALLED ->
                            entry.copy(status = QueueEntryStatus.ATTENDED, attendedAt = now())
                        entry.id == next?.id ->
                            entry.copy(status = QueueEntryStatus.CALLED, calledAt = now())
                        else -> entry
                    }
                },
            )
        }
    }

    // ---------------------------------------------------------------- consultas

    private fun reservationOf(appointmentId: Int): Reservation =
        reservations.firstOrNull { it.id == appointmentId }
            ?: throw CheckInException.ReservationNotFound(appointmentId)

    private fun queueOf(reservation: Reservation): AttendanceQueue =
        queues.first { it.timeSlotId == reservation.timeSlotId && it.date == reservation.date }

    private fun entryOf(tables: Tables, appointmentId: Int): QueueEntry? {
        val checkIn = tables.checkIns.firstOrNull { it.appointmentId == appointmentId } ?: return null
        return tables.entries.firstOrNull { it.checkInId == checkIn.id }
    }

    private fun ticketOf(tables: Tables, appointmentId: Int): AttendanceTicket? {
        val reservation = reservations.firstOrNull { it.id == appointmentId } ?: return null
        val checkIn = tables.checkIns.firstOrNull { it.appointmentId == appointmentId } ?: return null
        val entry = tables.entries.firstOrNull { it.checkInId == checkIn.id } ?: return null
        return AttendanceTicket(
            code = buildTicketCode(reservation.specialty, entry.position),
            reservation = reservation,
            checkIn = checkIn,
            queueEntry = entry,
        )
    }

    private fun positionOf(tables: Tables, appointmentId: Int): QueuePosition? {
        val mine = entryOf(tables, appointmentId)?.takeIf { it.isActive } ?: return null
        val active = tables.entries.filter { it.attendanceQueueId == mine.attendanceQueueId && it.isActive }
        val queue = queues.first { it.id == mine.attendanceQueueId }
        return QueuePosition(
            current = active.count { it.position < mine.position } + 1,
            total = active.size,
            queueStatus = queue.status,
            updatedAt = now().toLocalTime(),
        )
    }

    /** Inserta un check-in y su turno al final de la cola (posición = máximo + 1). */
    private fun Tables.withArrival(
        appointmentId: Int,
        queueId: Int,
        token: String,
        at: LocalDateTime,
    ): Tables {
        val checkIn = CheckIn(
            id = (checkIns.maxOfOrNull { it.id } ?: 0) + 1,
            appointmentId = appointmentId,
            qrToken = token,
            status = CheckInStatus.VALID,
            checkedInAt = at,
        )
        val entry = QueueEntry(
            id = (entries.maxOfOrNull { it.id } ?: 0) + 1,
            attendanceQueueId = queueId,
            checkInId = checkIn.id,
            position = (entries.filter { it.attendanceQueueId == queueId }.maxOfOrNull { it.position } ?: 0) + 1,
        )
        return copy(checkIns = checkIns + checkIn, entries = entries + entry)
    }

    private companion object {
        const val DEMO_QUEUE_ID = 9
        val DEMO_DATE: LocalDate = LocalDate.of(2026, 10, 6)
        val TOKEN_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMdd")

        /** `SY-<yyMMdd>-<appointments.id a 4 dígitos>`, p. ej. "SY-261006-0142". */
        fun qrToken(date: LocalDate, appointmentId: Int): String =
            "SY-${date.format(TOKEN_DATE)}-${appointmentId.toString().padStart(4, '0')}"
    }
}
