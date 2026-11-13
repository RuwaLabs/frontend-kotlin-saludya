package pe.edu.upc.saludya.features.iam.domain.repository

data class RecoveryHelpReceipt(val instructions: String?, val phone: String?)

interface RecoveryActionsRepository {
    suspend fun resetPassword(token: String, password: String, confirmation: String): AccessResult<Unit>
    suspend fun requestHelp(dni: String, contactEmail: String): AccessResult<RecoveryHelpReceipt>
    suspend fun requestRecoveryCode(email: String): AccessResult<PasswordRecoveryChallenge>
    suspend fun verifyRecoveryCode(challengeId: String, code: String): AccessResult<PasswordResetGrant>
    suspend fun resetPasswordWithGrant(
        resetToken: String,
        password: String,
        confirmation: String,
    ): AccessResult<Unit>
}
