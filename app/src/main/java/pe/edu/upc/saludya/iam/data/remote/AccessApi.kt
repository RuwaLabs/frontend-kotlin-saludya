package pe.edu.upc.saludya.iam.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET

data class LoginRequest(val email: String, val password: String) {
    override fun toString(): String = "LoginRequest[REDACTED]"
}
data class LoginChallengeResponse(val challengeId: String?, val maskedEmail: String?, val expiresAt: String?)
data class VerifyLoginRequest(val challengeId: String, val code: String) {
    override fun toString(): String = "VerifyLoginRequest[REDACTED]"
}
data class ResendLoginRequest(val challengeId: String)
data class PatientProfileResponse(val id: Long?, val patientId: Long?, val role: String?, val active: Boolean?,
    val name: String?, val lastname: String?, val email: String?)
data class SessionResponse(
    val accessToken: String?, val tokenType: String?, val expiresAt: String?,
    val userId: Long?, val role: String?, val patientId: Long?
) {
    override fun toString(): String = "SessionResponse[REDACTED]"
}

interface AccessApi {
    @GET("api/v1/user-accounts/me")
    suspend fun profile(@Header("Authorization") authorization: String): Response<PatientProfileResponse>
    @POST("api/v1/user-accounts/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginChallengeResponse>
    @POST("api/v1/user-accounts/login/verify")
    suspend fun verifyLogin(@Body request: VerifyLoginRequest): Response<SessionResponse>
    @POST("api/v1/user-accounts/login/resend")
    suspend fun resendLogin(@Body request: ResendLoginRequest): Response<Void>
    @POST("api/v1/user-accounts/recover-password")
    suspend fun recoverPassword(@Body request: EmailRequest): Response<Void>
    @POST("api/v1/user-accounts/logout")
    suspend fun logout(@Header("Authorization") authorization: String): Response<Void>
}
