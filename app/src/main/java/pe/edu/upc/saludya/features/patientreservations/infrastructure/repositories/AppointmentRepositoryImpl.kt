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
        val details = error?.details.orEmpty()
        return when {
            error?.code == "TIMESLOT_CONFLICT" -> TimeslotConflictException()
            error?.code == "APPOINTMENT_CONFLICT" -> AppointmentConflictException()
            details.contains("no available capacity", ignoreCase = true) -> TimeslotConflictException()
            details.contains("already has an active appointment", ignoreCase = true) ->
                AppointmentConflictException()
            details.contains("cancellation deadline", ignoreCase = true) ->
                Exception("No se pudo cancelar: ya pasó el plazo de 24 horas antes de la cita. Acércate a admisión.")
            details.contains("not active", ignoreCase = true) ->
                Exception("Esta cita ya no se puede cancelar.")
            details.contains("time slot is in the past", ignoreCase = true) ->
                Exception("No se pudo reservar: el servidor considera que ese horario ya pasó. Elige un horario más tarde.")
            details.contains("Booking is closed for today", ignoreCase = true) ->
                Exception("Las reservas para hoy ya cerraron. Elige otra fecha.")
            else -> Exception("No se pudo completar la operación. Intenta nuevamente.")
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
