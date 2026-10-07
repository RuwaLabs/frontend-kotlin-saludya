package pe.edu.upc.saludya.iam.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterPatientRequest(
    val dni: String,
    val name: String,
    val lastname: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val password: String
) {
    override fun toString(): String = "RegisterPatientRequest[REDACTED]"
}

data class RegisterPatientResponse(val id: Long? = null, val userId: Long? = null, val dni: String? = null)

interface RegistrationApi {
    @POST("api/v1/user-accounts")
    suspend fun register(@Body request: RegisterPatientRequest): Response<RegisterPatientResponse>
}
