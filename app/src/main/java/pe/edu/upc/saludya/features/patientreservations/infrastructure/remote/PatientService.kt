package pe.edu.upc.saludya.features.patientreservations.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PatientService {

    @GET("patients/{id}")
    suspend fun getPatient(@Path("id") id: Int): Response<PatientDto>

    @GET("patients/{id}/minors")
    suspend fun getMinors(@Path("id") id: Int): Response<List<PatientMinorDto>>
}
