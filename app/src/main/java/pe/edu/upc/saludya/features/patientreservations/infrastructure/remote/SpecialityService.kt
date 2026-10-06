package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SpecialityService {

    @GET("specialties")
    suspend fun getAllSpecialities(): Response<List<SpecialityDto>>

    @GET("specialties/{id}")
    suspend fun getSpecialityById(@Path("id") id: Int): Response<SpecialityDto>
}
