package pe.edu.upc.saludya.features.iam.infrastructure.repositories

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.features.iam.infrastructure.remote.*
import pe.edu.upc.saludya.features.iam.domain.repository.*
import retrofit2.Response
import java.io.IOException
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
    private fun failure(response: Response<*>): AccessResult.Failure {
        val code = response.errorBody()?.use {
            runCatching { gson.fromJson(it.string(), IdentityApiError::class.java)?.code }.getOrNull()
        }
        return AccessResult.Failure(when {
            code == "IAM_RESET_TOKEN_INVALID" -> AccessFailure.RESET_LINK_INVALID
            code == "IAM_PASSWORD_MISMATCH" -> AccessFailure.PASSWORD_MISMATCH
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
