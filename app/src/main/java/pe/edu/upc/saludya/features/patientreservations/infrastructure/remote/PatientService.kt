package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

data class LinkMinorRequest(
    val dni: String,
    val name: String,
    val lastname: String,
    val birthDate: String,
    val confirmFiliation: Boolean = true,
)

interface PatientService {

    @GET("patients/{id}")
    suspend fun getPatient(@Path("id") id: Int): Response<PatientDto>

    @GET("patients/{id}/minors")
    suspend fun getMinors(@Path("id") id: Int): Response<List<PatientMinorDto>>

    @DELETE("patient-minors/{id}")
    suspend fun unlinkMinor(@Path("id") id: Int): Response<Void>

    @POST("patient-minors")
    suspend fun linkMinor(@Body request: LinkMinorRequest): Response<PatientMinorDto>
}
