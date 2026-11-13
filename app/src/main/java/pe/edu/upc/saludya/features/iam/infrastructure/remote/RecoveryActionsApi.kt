package pe.edu.upc.saludya.features.iam.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class ResetPasswordRequest(val token: String, val password: String, val confirmPassword: String) {
    override fun toString(): String = "ResetPasswordRequest[REDACTED]"
}
data class RecoveryHelpRequest(val dni: String, val contactEmail: String)
data class RecoveryHelpResponse(val instructions: String?, val phone: String?)

data class RecoverPasswordRequest(val email: String)
data class PasswordRecoveryChallengeResponse(
    val challengeId: String?,
    val maskedEmail: String?,
    val expiresAt: String?,
)
data class VerifyRecoveryCodeRequest(val challengeId: String, val code: String) {
    override fun toString(): String = "VerifyRecoveryCodeRequest[REDACTED]"
}
data class PasswordResetGrantResponse(val resetToken: String?, val expiresAt: String?)
data class ResetPasswordWithGrantRequest(
    val resetToken: String,
    val password: String,
    val confirmPassword: String,
) {
    override fun toString(): String = "ResetPasswordWithGrantRequest[REDACTED]"
}

interface RecoveryActionsApi {
    @POST("user-accounts/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<Void>
    @POST("account-recovery-requests")
    suspend fun requestHelp(@Body request: RecoveryHelpRequest): Response<RecoveryHelpResponse>
    @POST("user-accounts/recover-password/code")
    suspend fun requestRecoveryCode(@Body request: RecoverPasswordRequest): Response<PasswordRecoveryChallengeResponse>
    @POST("user-accounts/recover-password/code/verify")
    suspend fun verifyRecoveryCode(@Body request: VerifyRecoveryCodeRequest): Response<PasswordResetGrantResponse>
    @POST("user-accounts/recover-password/code/reset")
    suspend fun resetPasswordWithGrant(@Body request: ResetPasswordWithGrantRequest): Response<Void>
}
