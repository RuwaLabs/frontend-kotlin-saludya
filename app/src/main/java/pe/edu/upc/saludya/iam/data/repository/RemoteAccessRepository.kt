package pe.edu.upc.saludya.iam.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.iam.data.remote.*
import pe.edu.upc.saludya.iam.domain.repository.*
import retrofit2.Response
import java.io.IOException
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

class RemoteAccessRepository @Inject constructor(
    private val api: AccessApi, private val gson: Gson, private val clock: Clock
) : AccessRepository {
    override suspend fun profile(token: String): AccessResult<PatientAccountProfile> = safely {
        val response = api.profile("Bearer $token")
        if (response.code() == 401) return@safely AccessResult.Failure(AccessFailure.SESSION_EXPIRED)
        if (response.code() != 200) return@safely failure(response)
        val body = response.body()
        if (body?.active == false) return@safely AccessResult.Failure(AccessFailure.SESSION_EXPIRED)
        if (body?.role != null && body.role != "PATIENT") return@safely AccessResult.Failure(AccessFailure.NOT_PATIENT)
        if (body?.role != "PATIENT" || body.active != true || body.id == null || body.id <= 0 ||
            body.patientId == null || body.patientId <= 0 || body.name.isNullOrBlank() || body.lastname.isNullOrBlank() ||
            body.email.isNullOrBlank()) AccessResult.Failure(AccessFailure.UNEXPECTED)
        else AccessResult.Success(PatientAccountProfile(body.id, body.patientId, body.name, body.lastname, body.email))
    }
    override suspend fun login(email: String, password: String): AccessResult<LoginChallenge> = safely {
        val response = api.login(LoginRequest(email, password))
        if (response.code() != 200) return@safely failure(response)
        val body = response.body()
        val expires = body?.expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
        val id = body?.challengeId
        val recipient = body?.maskedEmail
        if (id.isNullOrBlank() || recipient.isNullOrBlank() || expires == null || !expires.isAfter(clock.instant())) {
            AccessResult.Failure(AccessFailure.UNEXPECTED)
        } else AccessResult.Success(LoginChallenge(id, recipient, expires))
    }

    override suspend fun verifyLogin(challengeId: String, code: String): AccessResult<PatientSession> = safely {
        val response = api.verifyLogin(VerifyLoginRequest(challengeId, code))
        if (response.code() != 200) return@safely failure(response)
        val body = response.body()
        val expires = body?.expiresAt?.let { runCatching { Instant.parse(it) }.getOrNull() }
        if (body?.role != null && body.role != "PATIENT") return@safely AccessResult.Failure(AccessFailure.NOT_PATIENT)
        val token = body?.accessToken
        val userId = body?.userId
        val patientId = body?.patientId
        if (body?.role != "PATIENT" || body.tokenType != "Bearer" || token.isNullOrBlank() ||
            token.any { it.isWhitespace() || it.isISOControl() } || expires == null || !expires.isAfter(clock.instant()) ||
            userId == null || userId <= 0 || patientId == null || patientId <= 0) {
            AccessResult.Failure(AccessFailure.UNEXPECTED)
        } else AccessResult.Success(PatientSession(token, expires, userId, patientId))
    }

    override suspend fun resendLogin(challengeId: String): AccessResult<Unit> = safely {
        val response = api.resendLogin(ResendLoginRequest(challengeId))
        if (response.code() == 202) AccessResult.Success(Unit) else failure(response)
    }

    override suspend fun recoverPassword(email: String): AccessResult<Unit> = safely {
        val response = api.recoverPassword(EmailRequest(email))
        // Never expose whether the address belongs to an account, including legacy 404 responses.
        if (response.code() == 202 || response.code() == 404) AccessResult.Success(Unit) else failure(response)
    }

    override suspend fun logout(token: String): AccessResult<Unit> = safely {
        val response = api.logout("Bearer $token")
        if (response.code() == 204 || response.code() == 401) AccessResult.Success(Unit) else failure(response)
    }

    private fun failure(response: Response<*>): AccessResult.Failure {
        val code = response.errorBody()?.use {
            runCatching { gson.fromJson(it.string(), IdentityApiError::class.java)?.code }.getOrNull()
        }
        return AccessResult.Failure(when {
            code == "IAM_ACCOUNT_INACTIVE" || code == "IAM_INACTIVE_ACCOUNT" -> AccessFailure.INACTIVE_ACCOUNT
            response.code() == 401 -> AccessFailure.INVALID_CREDENTIALS
            code == "IAM_INVALID_EMAIL_CODE" -> AccessFailure.INVALID_CODE
            code in setOf("IAM_EMAIL_CODE_EXPIRED", "IAM_LOGIN_CHALLENGE_EXPIRED", "IAM_INVALID_LOGIN_CHALLENGE") -> AccessFailure.EXPIRED_CODE
            response.code() == 429 -> AccessFailure.TOO_MANY_ATTEMPTS
            response.code() == 400 -> AccessFailure.INVALID_REQUEST
            response.code() == 408 || response.code() in 500..599 -> AccessFailure.UNAVAILABLE
            else -> AccessFailure.UNEXPECTED
        })
    }

    private suspend fun <T> safely(block: suspend () -> AccessResult<T>): AccessResult<T> = try {
        block()
    } catch (cancelled: CancellationException) {
        throw cancelled
    } catch (_: MalformedJsonException) {
        AccessResult.Failure(AccessFailure.UNEXPECTED)
    } catch (_: JsonParseException) {
        AccessResult.Failure(AccessFailure.UNEXPECTED)
    } catch (_: IOException) {
        AccessResult.Failure(AccessFailure.UNAVAILABLE)
    }
}
