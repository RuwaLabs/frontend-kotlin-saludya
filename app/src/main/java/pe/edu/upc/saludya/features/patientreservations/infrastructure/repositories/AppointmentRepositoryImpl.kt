package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import com.google.gson.Gson
import pe.edu.upc.saludya.features.patientreservations.domain.model.Appointment
import pe.edu.upc.saludya.features.patientreservations.domain.model.AppointmentConflictException
import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeslotConflictException
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper.toDomain
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.BookAppointmentRequestDto
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.ErrorDto
import retrofit2.Response
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val service: AppointmentService
) : AppointmentRepository {

    private val gson = Gson()

    override suspend fun bookAppointment(
        patientId: Int,
        timeSlotId: Int
    ): Result<Appointment> = try {
        service.bookAppointment(
            BookAppointmentRequestDto(patientId = patientId, timeSlotId = timeSlotId)
        ).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun cancelAppointment(id: Int): Result<Appointment> = try {
        service.cancelAppointment(id).toResult()
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAppointmentById(id: Int): Appointment? = try {
        val response = service.getAppointmentById(id)
        if (response.isSuccessful) response.body()?.toDomain() else null
    } catch (_: Exception) {
        null
    }

    override suspend fun getAppointmentsByPatientId(patientId: Int): List<Appointment> = try {
        val response = service.getAppointmentsByPatientId(patientId)
        if (response.isSuccessful) response.body().orEmpty().map { it.toDomain() } else emptyList()
    } catch (_: Exception) {
        emptyList()
    }

    override suspend fun getAppointmentsByTimeSlotId(timeSlotId: Int): List<Appointment> = try {
        val response = service.getAppointmentsByTimeSlotId(timeSlotId)
        if (response.isSuccessful) response.body().orEmpty().map { it.toDomain() } else emptyList()
    } catch (_: Exception) {
        emptyList()
    }

    override suspend fun getAppointmentsBySpecialityId(specialityId: Int): List<Appointment> = try {
        val response = service.getAppointmentsBySpecialityId(specialityId)
        if (response.isSuccessful) response.body().orEmpty().map { it.toDomain() } else emptyList()
    } catch (_: Exception) {
        emptyList()
    }

    private fun Response<AppointmentDto>.toResult(): Result<Appointment> {
        val body = body()
        return if (isSuccessful && body != null) {
            Result.success(body.toDomain())
        } else {
            Result.failure(toFailure())
        }
    }

    private fun Response<*>.toFailure(): Exception {
        val error = parseError()
        return when (error?.code) {
            "TIMESLOT_CONFLICT" -> TimeslotConflictException()
            "APPOINTMENT_CONFLICT" -> AppointmentConflictException()
            else -> Exception(error?.message ?: "Error ${code()}")
        }
    }

    private fun Response<*>.parseError(): ErrorDto? {
        val raw = errorBody()?.string() ?: return null
        return try {
            gson.fromJson(raw, ErrorDto::class.java)
        } catch (_: Exception) {
            null
        }
    }
}
