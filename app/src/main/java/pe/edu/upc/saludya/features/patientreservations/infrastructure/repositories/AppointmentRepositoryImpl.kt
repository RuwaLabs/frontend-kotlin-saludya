package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Appointment
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.BookAppointmentRequestDto
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val service: AppointmentService
) : AppointmentRepository {

    override suspend fun bookAppointment(patientId: Int, timeSlotId: Int): Appointment? {
        try {
            val response = service.bookAppointment(
                BookAppointmentRequestDto(patientId = patientId, timeSlotId = timeSlotId)
            )
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    return Appointment(
                        id = dto.id,
                        timeSlotId = dto.timeSlotId,
                        patientId = dto.patientId,
                        bookingOrder = dto.bookingOrder,
                        bookingCode = dto.bookingCode,
                        status = dto.status,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getAppointmentById(id: Int): Appointment? {
        try {
            val response = service.getAppointmentById(id)
            if (response.isSuccessful) {
                response.body()?.let { dto ->
                    return Appointment(
                        id = dto.id,
                        timeSlotId = dto.timeSlotId,
                        patientId = dto.patientId,
                        bookingOrder = dto.bookingOrder,
                        bookingCode = dto.bookingCode,
                        status = dto.status,
                        createdAt = dto.createdAt,
                        updatedAt = dto.updatedAt
                    )
                }
            }
        } catch (_: Exception) {
        }
        return null
    }

    override suspend fun getAppointmentsByPatientId(patientId: Int): List<Appointment> {
        try {
            val response = service.getAppointmentsByPatientId(patientId)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        Appointment(
                            id = dto.id,
                            timeSlotId = dto.timeSlotId,
                            patientId = dto.patientId,
                            bookingOrder = dto.bookingOrder,
                            bookingCode = dto.bookingCode,
                            status = dto.status,
                            createdAt = dto.createdAt,
                            updatedAt = dto.updatedAt
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }

    override suspend fun getAppointmentsByTimeSlotId(timeSlotId: Int): List<Appointment> {
        try {
            val response = service.getAppointmentsByTimeSlotId(timeSlotId)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        Appointment(
                            id = dto.id,
                            timeSlotId = dto.timeSlotId,
                            patientId = dto.patientId,
                            bookingOrder = dto.bookingOrder,
                            bookingCode = dto.bookingCode,
                            status = dto.status,
                            createdAt = dto.createdAt,
                            updatedAt = dto.updatedAt
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }

    override suspend fun getAppointmentsBySpecialityId(specialityId: Int): List<Appointment> {
        try {
            val response = service.getAppointmentsBySpecialityId(specialityId)
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    return dtos.map { dto ->
                        Appointment(
                            id = dto.id,
                            timeSlotId = dto.timeSlotId,
                            patientId = dto.patientId,
                            bookingOrder = dto.bookingOrder,
                            bookingCode = dto.bookingCode,
                            status = dto.status,
                            createdAt = dto.createdAt,
                            updatedAt = dto.updatedAt
                        )
                    }
                }
            }
        } catch (_: Exception) {
        }
        return emptyList()
    }
}
