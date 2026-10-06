package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeSlot
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.TimeSlotRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotService
import javax.inject.Inject

class TimeSlotRepositoryImpl @Inject constructor(
    private val service: TimeSlotService
) : TimeSlotRepository {

    override suspend fun getTimeSlotById(id: Int): TimeSlot? {
        try {
            val response = service.getTimeSlotById(id)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    return TimeSlot(
                        id = dto.id,
                        doctorId = dto.doctorId,
                        date = dto.date,
                        startHour = dto.startHour,
                        endHour = dto.endHour,
                        room = dto.room,
                        maxCapacity = dto.maxCapacity,
                        currentBookings = dto.currentBookings,
                        status = dto.status
                    )
                }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getTimeSlotAvailableBySpecialityId(
        specialityId: Int,
        date: String
    ): List<TimeSlot> {
        try {
            val response = service.getTimeSlotAvailableBySpecialityId(specialityId, date)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        TimeSlot(
                            id = dto.id,
                            doctorId = dto.doctorId,
                            date = dto.date,
                            startHour = dto.startHour,
                            endHour = dto.endHour,
                            room = dto.room,
                            maxCapacity = dto.maxCapacity,
                            currentBookings = dto.currentBookings,
                            status = dto.status
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }

    override suspend fun getTimeSlotByDoctorIdAndDate(
        doctorId: Int,
        date: String
    ): List<TimeSlot> {
        try {
            val response = service.getTimeSlotByDoctorIdAndDate(doctorId, date)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        TimeSlot(
                            id = dto.id,
                            doctorId = dto.doctorId,
                            date = dto.date,
                            startHour = dto.startHour,
                            endHour = dto.endHour,
                            room = dto.room,
                            maxCapacity = dto.maxCapacity,
                            currentBookings = dto.currentBookings,
                            status = dto.status
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }
}
