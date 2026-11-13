package pe.edu.upc.saludya.features.home.infrastructure.repositories

import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.home.domain.HomeAppointment
import pe.edu.upc.saludya.features.home.domain.HomeRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotService
import java.time.LocalDate
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val appointments: AppointmentService,
    private val timeSlots: TimeSlotService,
    private val doctors: DoctorService,
    private val specialties: SpecialityService,
    private val patients: PatientService,
    private val session: SessionProvider,
) : HomeRepository {

    override suspend fun getUpcoming(): List<HomeAppointment> = getAll().filter { it.isUpcoming }

    override suspend fun getHistory(): List<HomeAppointment> = getAll().filterNot { it.isUpcoming }

    override suspend fun getById(id: Long): HomeAppointment? {
        val dto = appointments.getAppointmentById(id.toInt()).body() ?: return null
        return toHomeAppointment(dto)
    }

    private suspend fun getAll(): List<HomeAppointment> {
        val patientId = session.patientId ?: error("Inicia sesión para consultar tus citas")
        val response = appointments.getAppointmentsByPatientId(patientId)
        if (!response.isSuccessful) error("No se pudieron cargar tus citas: ${response.code()}")
        return response.body().orEmpty().map { toHomeAppointment(it) }
    }

    private suspend fun toHomeAppointment(appointment: AppointmentDto): HomeAppointment {
        val slot = timeSlots.getTimeSlotById(appointment.timeSlotId).body()
            ?: error("No se encontró el horario de la cita")
        val doctor = doctors.getDoctorById(slot.doctorId).body()
            ?: error("No se encontró el profesional de la cita")
        val specialty = specialties.getSpecialityById(doctor.specialtyId).body()
            ?: error("No se encontró la especialidad de la cita")
        val patient = patients.getPatient(appointment.patientId).body()
            ?: error("No se encontró el paciente de la cita")
        val active = appointment.status in setOf("RESERVED", "CONFIRMED")
        val upcoming = active && !LocalDate.parse(slot.date).isBefore(LocalDate.now())

        return HomeAppointment(
            id = appointment.id.toLong(),
            specialty = specialty.name,
            patientName = "${patient.name} ${patient.lastname}",
            doctorName = "${doctor.name} ${doctor.lastname}",
            date = slot.date,
            time = "${slot.startHour}–${slot.endHour}",
            place = slot.room,
            status = appointment.status,
            bookingCode = appointment.bookingCode,
            isUpcoming = upcoming,
        )
    }
}
