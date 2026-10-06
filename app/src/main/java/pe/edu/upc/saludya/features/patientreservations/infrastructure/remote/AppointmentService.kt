package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AppointmentService {

    @POST("appointments")
    suspend fun bookAppointment(
        @Body request: BookAppointmentRequestDto
    ): Response<AppointmentDto>

    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: Int): Response<AppointmentDto>

    @GET("appointments/patient/{patientId}")
    suspend fun getAppointmentsByPatientId(
        @Path("patientId") patientId: Int
    ): Response<List<AppointmentDto>>

    @GET("appointments")
    suspend fun getAppointmentsByTimeSlotId(
        @Query("timeSlotId") timeSlotId: Int
    ): Response<List<AppointmentDto>>

    @GET("appointments")
    suspend fun getAppointmentsBySpecialityId(
        @Query("specialtyId") specialityId: Int
    ): Response<List<AppointmentDto>>
}
