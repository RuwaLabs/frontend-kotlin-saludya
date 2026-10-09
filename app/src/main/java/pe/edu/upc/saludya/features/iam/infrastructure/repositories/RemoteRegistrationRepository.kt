package pe.edu.upc.saludya.features.iam.infrastructure.repositories

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.features.iam.infrastructure.remote.*
import pe.edu.upc.saludya.features.iam.domain.model.RegistrationCredentials
import pe.edu.upc.saludya.features.iam.domain.model.VerifiedIdentity
import pe.edu.upc.saludya.features.iam.domain.repository.*
import java.io.IOException
import javax.inject.Inject

class RemoteRegistrationRepository @Inject constructor(
    private val api: RegistrationApi,
    private val gson: Gson
) : RegistrationRepository {
    override suspend fun sendCode(email: String): RegistrationResult = safely {
        val response = api.sendCode(EmailRequest(email))
        if (response.code() == 202) RegistrationResult.CodeSent else failure(response)
    }

    override suspend fun register(identity: VerifiedIdentity, credentials: RegistrationCredentials, code: String): RegistrationResult {
        val details = identity.details
        return safely {
            val response = api.register(RegisterPatientRequest(details.dni, details.names,
                details.lastNames, details.birthDate.toString(), credentials.phone, credentials.email, credentials.password, code))
            if (response.code() == 201) {
                val body = response.body()
                val patientId = body?.id
                val accountId = body?.userId
                if (patientId != null && patientId > 0 && accountId != null && accountId > 0 && body.dni == details.dni) {
                    RegistrationResult.Accepted(RegistrationReceipt(patientId, accountId, credentials.email))
                } else RegistrationResult.UnexpectedError
            } else {
                failure(response)
            }
        }
    }

    private fun failure(response: retrofit2.Response<*>): RegistrationResult {
        val code = response.errorBody()?.use {
            runCatching { gson.fromJson(it.string(), IdentityApiError::class.java)?.code }.getOrNull()
        }
        return when {
            response.code() == 422 && code == "IAM_INVALID_EMAIL_CODE" -> RegistrationResult.InvalidCode
            response.code() == 422 && code == "IAM_EMAIL_CODE_EXPIRED" -> RegistrationResult.ExpiredCode
            response.code() == 429 && code == "IAM_EMAIL_TOO_MANY_ATTEMPTS" -> RegistrationResult.TooManyAttempts
            response.code() == 409 && code == "IAM_EMAIL_ALREADY_REGISTERED" -> RegistrationResult.EmailInUse
            response.code() == 409 && code == "IAM_DNI_ALREADY_REGISTERED" -> RegistrationResult.IdentityAlreadyRegistered
            response.code() == 409 && code == "IAM_DUPLICATE_RECORD" -> RegistrationResult.DuplicateRecord
            response.code() == 422 && code == "IAM_IDENTITY_MISMATCH" -> RegistrationResult.IdentityMismatch
            response.code() == 422 && code == "IAM_ADULT_REQUIRED" -> RegistrationResult.AdultRequired
            response.code() == 400 -> RegistrationResult.InvalidRequest
            response.code() in 500..599 || response.code() == 408 || response.code() == 429 -> RegistrationResult.Unavailable
            else -> RegistrationResult.UnexpectedError
        }
    }

    private suspend fun safely(block: suspend () -> RegistrationResult): RegistrationResult = try {
        block()
    } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: MalformedJsonException) {
            RegistrationResult.UnexpectedError
        } catch (_: JsonParseException) {
            RegistrationResult.UnexpectedError
        } catch (_: IOException) {
            RegistrationResult.Unavailable
    }
}
