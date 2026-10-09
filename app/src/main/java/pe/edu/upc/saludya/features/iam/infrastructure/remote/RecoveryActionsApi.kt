package pe.edu.upc.saludya.features.iam.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class ResetPasswordRequest(val token: String, val password: String, val confirmPassword: String) {
    override fun toString(): String = "ResetPasswordRequest[REDACTED]"
}
data class RecoveryHelpRequest(val dni: String, val contactEmail: String)
data class RecoveryHelpResponse(val instructions: String?, val phone: String?)

interface RecoveryActionsApi {
    @POST("user-accounts/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Void>
    @POST("account-recovery-requests")
    suspend fun requestHelp(@Body request: RecoveryHelpRequest): Response<RecoveryHelpResponse>
}
