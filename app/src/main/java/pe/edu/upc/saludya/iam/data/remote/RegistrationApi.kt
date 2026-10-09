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
    val password: String,
    val code: String
) {
    override fun toString(): String = "RegisterPatientRequest[REDACTED]"
}

data class RegisterPatientResponse(val id: Long? = null, val userId: Long? = null, val dni: String? = null)

interface RegistrationApi {
    @POST("user-accounts/send-verification-code")
    suspend fun sendCode(@Body request: EmailRequest): Response<Void>
    @POST("user-accounts")
    suspend fun register(@Body request: RegisterPatientRequest): Response<RegisterPatientResponse>
}

data class EmailRequest(val email: String)
