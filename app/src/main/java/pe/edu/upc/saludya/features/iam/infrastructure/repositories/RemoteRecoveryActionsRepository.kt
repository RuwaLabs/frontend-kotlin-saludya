package pe.edu.upc.saludya.features.iam.infrastructure.repositories

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.features.iam.infrastructure.remote.*
import pe.edu.upc.saludya.features.iam.domain.repository.*
import retrofit2.Response
import java.io.IOException
import java.time.Instant
import javax.inject.Inject

class RemoteRecoveryActionsRepository @Inject constructor(
    private val api: RecoveryActionsApi, private val gson: Gson
) : RecoveryActionsRepository {
    override suspend fun resetPassword(token: String, password: String, confirmation: String): AccessResult<Unit> = safely {
        val response = api.resetPassword(ResetPasswordRequest(token, password, confirmation))
        if (response.code() == 204) AccessResult.Success(Unit) else failure(response)
    }
    override suspend fun requestHelp(dni: String, contactEmail: String): AccessResult<RecoveryHelpReceipt> = safely {
        val response = api.requestHelp(RecoveryHelpRequest(dni, contactEmail))
        if (response.code() == 202) {
            val body = response.body()
            AccessResult.Success(RecoveryHelpReceipt(body?.instructions, body?.phone))
        } else failure(response)
    }

    override suspend fun requestRecoveryCode(email: String): AccessResult<PasswordRecoveryChallenge> = safely {
        val response = api.requestRecoveryCode(RecoverPasswordRequest(email))
        if (response.code() != 200) return@safely failure(response)
        val body = response.body()
        val expiresAt = body?.expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
        val challengeId = body?.challengeId
        val maskedEmail = body?.maskedEmail
        if (challengeId.isNullOrBlank() || maskedEmail.isNullOrBlank() || expiresAt == null) {
            AccessResult.Failure(AccessFailure.UNEXPECTED)
        } else {
            AccessResult.Success(PasswordRecoveryChallenge(challengeId, maskedEmail, expiresAt))
        }
    }

    override suspend fun verifyRecoveryCode(challengeId: String, code: String): AccessResult<PasswordResetGrant> = safely {
        val response = api.verifyRecoveryCode(VerifyRecoveryCodeRequest(challengeId, code))
        if (response.code() != 200) return@safely failure(response)
        val body = response.body()
        val resetToken = body?.resetToken
        val expiresAt = body?.expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
        if (resetToken.isNullOrBlank() || expiresAt == null) {
            AccessResult.Failure(AccessFailure.UNEXPECTED)
        } else {
            AccessResult.Success(PasswordResetGrant(resetToken, expiresAt))
        }
    }

    override suspend fun resetPasswordWithGrant(
        resetToken: String,
        password: String,
        confirmation: String,
    ): AccessResult<Unit> = safely {
        val response = api.resetPasswordWithGrant(
            ResetPasswordWithGrantRequest(resetToken, password, confirmation)
        )
        if (response.code() == 200) AccessResult.Success(Unit) else failure(response)
    }
    private fun failure(response: Response<*>): AccessResult.Failure {
        val code = response.errorBody()?.use {
            runCatching { gson.fromJson(it.string(), IdentityApiError::class.java)?.code }.getOrNull()
        }
        return AccessResult.Failure(when {
            code == "IAM_RESET_TOKEN_INVALID" || code == "IAM_RESET_GRANT_INVALID" ->
                AccessFailure.RESET_LINK_INVALID
            code == "IAM_PASSWORD_MISMATCH" -> AccessFailure.PASSWORD_MISMATCH
            code == "IAM_INVALID_EMAIL_CODE" || code == "IAM_INVALID_RECOVERY_CODE" ->
                AccessFailure.INVALID_CODE
            code in setOf("IAM_EMAIL_CODE_EXPIRED", "IAM_RECOVERY_CODE_EXPIRED") ->
                AccessFailure.EXPIRED_CODE
            response.code() == 400 -> AccessFailure.INVALID_REQUEST
            response.code() == 429 -> AccessFailure.TOO_MANY_ATTEMPTS
            response.code() == 408 || response.code() in 500..599 -> AccessFailure.UNAVAILABLE
            else -> AccessFailure.UNEXPECTED
        })
    }
    private suspend fun <T> safely(block: suspend () -> AccessResult<T>): AccessResult<T> = try {
        block()
    } catch (cancelled: CancellationException) { throw cancelled
    } catch (_: MalformedJsonException) { AccessResult.Failure(AccessFailure.UNEXPECTED)
    } catch (_: JsonParseException) { AccessResult.Failure(AccessFailure.UNEXPECTED)
    } catch (_: IOException) { AccessResult.Failure(AccessFailure.UNAVAILABLE) }
}
