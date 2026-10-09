package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DoctorService {

    @GET("doctors")
    suspend fun getDoctorsBySpecialityId(
        @Query("specialtyId") specialityId: Int
    ): Response<List<DoctorDto>>

    @GET("doctors/{id}")
    suspend fun getDoctorById(@Path("id") id: Int): Response<DoctorDto>
}
