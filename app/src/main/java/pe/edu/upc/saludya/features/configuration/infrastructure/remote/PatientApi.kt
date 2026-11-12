package pe.edu.upc.saludya.features.configuration.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class AccountProfileDto(
    val id: Long?,
    val role: String?,
    val email: String?,
    val active: Boolean?,
    val patientId: Long?,
    val dni: String?,
    val name: String?,
    val lastname: String?,
    val birthDate: String?,
    val phone: String?,
)

data class PatientDto(
    val id: Long?,
    val userId: Long?,
    val dni: String?,
    val name: String?,
    val lastname: String?,
    val birthDate: String?,
    val phone: String?,
)

interface PatientApi {

    @GET("api/v1/user-accounts/me")
    suspend fun getMyAccount(
        @Header("Authorization") authorization: String,
    ): Response<AccountProfileDto>

    @GET("api/v1/patients/{id}")
    suspend fun getPatient(
        @Path("id") patientId: Long,
        @Header("Authorization") authorization: String,
    ): Response<PatientDto>
}