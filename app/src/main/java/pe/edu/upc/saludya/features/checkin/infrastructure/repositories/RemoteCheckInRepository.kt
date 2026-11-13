package pe.edu.upc.saludya.features.checkin.infrastructure.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.checkin.domain.model.AppointmentStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.model.CheckIn
import pe.edu.upc.saludya.features.checkin.domain.model.CheckInStatus
import pe.edu.upc.saludya.features.checkin.domain.model.HospitalConfiguration
import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import pe.edu.upc.saludya.features.checkin.domain.model.QueuePosition
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository
import pe.edu.upc.saludya.features.checkin.infrastructure.remote.CheckInApi
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotService
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class RemoteCheckInRepository @Inject constructor(
    private val api: CheckInApi,
    private val appointments: AppointmentService,
    private val timeSlots: TimeSlotService,
    private val doctors: DoctorService,
    private val specialties: SpecialityService,
    private val patients: PatientService,
    private val session: SessionProvider,
) : CheckInRepository {

    private val refreshes = MutableStateFlow(0)

    private fun today(): LocalDate = LocalDate.now(ZoneId.of("America/Lima"))

    private fun requireSameDay(date: LocalDate) {
        if (date != today()) {
            error("El código y la cola solo están disponibles el día de la cita.")
        }
    }

    override suspend fun getReservations(): List<Reservation> {
        val patientId = session.patientId ?: error("Inicia sesión para consultar tus reservas")
        val response = appointments.getAppointmentsByPatientId(patientId)
        if (!response.isSuccessful) error("No se pudieron consultar las reservas: ${response.code()}")
        return response.body().orEmpty()
            .filter { it.status == "RESERVED" || it.status == "CONFIRMED" }
            .map { toReservation(it) }
    }

    private suspend fun toReservation(appointment: AppointmentDto): Reservation {
        val slot = timeSlots.getTimeSlotById(appointment.timeSlotId).body()
            ?: error("No se encontró el horario")
        val doctor = doctors.getDoctorById(slot.doctorId).body()
            ?: error("No se encontró el profesional")
        val specialty = specialties.getSpecialityById(doctor.specialtyId).body()
            ?: error("No se encontró la especialidad")
        val patient = patients.getPatient(appointment.patientId).body()
            ?: error("No se encontró el paciente")
        return Reservation(
            id = appointment.id,
            timeSlotId = slot.id,
            bookingOrder = appointment.bookingOrder,
            status = AppointmentStatus.valueOf(appointment.status),
            patientFullName = "${patient.name} ${patient.lastname}",
            specialty = specialty.name,
            doctorFullName = "${doctor.name} ${doctor.lastname}",
            date = LocalDate.parse(slot.date),
            startHour = LocalTime.parse(slot.startHour),
            endHour = LocalTime.parse(slot.endHour),
            consultingRoom = slot.room,
        )
    }

    override suspend fun getQrCode(appointmentId: Int): QrCodeInfo {
        val appointment = appointments.getAppointmentById(appointmentId).body()
            ?: error("No se encontró la reserva")
        val slot = timeSlots.getTimeSlotById(appointment.timeSlotId).body()
            ?: error("No se encontró el horario")
        requireSameDay(LocalDate.parse(slot.date))
        // El código de reserva es real y admisión puede ingresarlo manualmente.
        // La imagen sigue siendo una muestra no escaneable.
        return QrCodeInfo(appointment.bookingCode, appointmentId, isSample = true)
    }

    override suspend fun registerArrival(appointmentId: Int): AttendanceTicket =
        error("El registro de llegada debe hacerlo el personal de admisión")

    override fun observeTicket(appointmentId: Int): Flow<AttendanceTicket?> = refreshes.map {
        val response = api.getTicket(appointmentId)
        if (response.code() == 404) return@map null
        if (!response.isSuccessful) error("No se pudo consultar el ticket: ${response.code()}")
        val ticket = response.body() ?: error("El ticket no tiene datos")
        val appointment = appointments.getAppointmentById(appointmentId).body()
            ?: error("No se encontró la reserva")
        val reservation = toReservation(appointment)
        requireSameDay(reservation.date)
        AttendanceTicket(
            code = ticket.turnCode ?: ticket.bookingCode ?: appointment.bookingCode,
            reservation = reservation,
            checkIn = CheckIn(
                id = ticket.id,
                appointmentId = appointmentId,
                qrToken = null,
                status = CheckInStatus.valueOf(ticket.status),
                checkedInAt = Instant.parse(ticket.checkedInAt)
                    .atZone(ZoneId.of("America/Lima")).toLocalDateTime(),
            ),
        )
    }

    override fun observeQueuePosition(appointmentId: Int): Flow<QueuePosition?> = refreshes.map {
        val appointment = appointments.getAppointmentById(appointmentId).body()
            ?: error("No se encontró la reserva")
        val slot = timeSlots.getTimeSlotById(appointment.timeSlotId).body()
            ?: error("No se encontró el horario")
        requireSameDay(LocalDate.parse(slot.date))
        val response = api.getPosition(appointmentId)
        if (response.code() == 404) return@map null
        if (!response.isSuccessful) error("No se pudo consultar la cola: ${response.code()}")
        val position = response.body() ?: error("La cola no tiene datos")
        QueuePosition(
            current = position.position,
            total = position.totalInQueue,
            updatedAt = LocalTime.now(ZoneId.of("America/Lima")),
        )
    }

    override fun refresh(appointmentId: Int) { refreshes.update { it + 1 } }

    override suspend fun leaveQueue(appointmentId: Int) {
        error("Consulta con admisión para abandonar la cola")
    }

    override suspend fun getConfiguration(): HospitalConfiguration =
        error("La configuración del establecimiento no está disponible para pacientes")
}
