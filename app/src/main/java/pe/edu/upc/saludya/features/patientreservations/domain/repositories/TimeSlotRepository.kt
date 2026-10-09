package pe.edu.upc.saludya.features.patientreservations.domain.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeSlot

interface TimeSlotRepository {
    suspend fun getTimeSlotById(id: Int): TimeSlot?
    suspend fun getTimeSlotAvailableBySpecialityId(specialityId: Int, date: String): List<TimeSlot>
    suspend fun getTimeSlotByDoctorIdAndDate(doctorId: Int, date: String): List<TimeSlot>
}
