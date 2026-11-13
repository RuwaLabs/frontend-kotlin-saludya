package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TimeSlotService {

    @GET("time-slots/{id}")
    suspend fun getTimeSlotById(@Path("id") id: Int): Response<TimeSlotDto>

    @GET("time-slots/available")
    suspend fun getTimeSlotAvailableBySpecialityId(
        @Query("specialtyId") specialityId: Int,
        @Query("date") date: String
    ): Response<List<TimeSlotDto>>

    @GET("time-slots")
    suspend fun getTimeSlotByDoctorIdAndDate(
        @Query("doctorId") doctorId: Int,
        @Query("date") date: String
    ): Response<List<TimeSlotDto>>
}
