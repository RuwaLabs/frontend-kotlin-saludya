package pe.edu.upc.saludya.features.patientreservations.domain.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Appointment

interface AppointmentRepository {
    suspend fun bookAppointment(patientId: Int, timeSlotId: Int): Result<Appointment>
    suspend fun cancelAppointment(id: Int): Result<Appointment>
    suspend fun getAppointmentById(id: Int): Appointment?
    suspend fun getAppointmentsByPatientId(patientId: Int): List<Appointment>
    suspend fun getAppointmentsByTimeSlotId(timeSlotId: Int): List<Appointment>
    suspend fun getAppointmentsBySpecialityId(specialityId: Int): List<Appointment>
}
