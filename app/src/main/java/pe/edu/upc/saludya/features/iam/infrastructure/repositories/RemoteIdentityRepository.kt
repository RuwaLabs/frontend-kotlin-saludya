package pe.edu.upc.saludya.features.iam.infrastructure.repositories

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import pe.edu.upc.saludya.features.iam.infrastructure.remote.IdentityApi
import pe.edu.upc.saludya.features.iam.infrastructure.remote.IdentityApiError
import pe.edu.upc.saludya.features.iam.infrastructure.remote.VerifyIdentityRequest
import pe.edu.upc.saludya.features.iam.domain.model.IdentityDetails
import pe.edu.upc.saludya.features.iam.domain.model.VerifiedIdentity
import pe.edu.upc.saludya.features.iam.domain.repository.IdentityRepository
import pe.edu.upc.saludya.features.iam.domain.repository.IdentityVerificationResult
import java.io.IOException
import javax.inject.Inject

class RemoteIdentityRepository @Inject constructor(
    private val api: IdentityApi,
    private val gson: Gson
) : IdentityRepository {
    override suspend fun verify(details: IdentityDetails): IdentityVerificationResult {
        return try {
            val response = api.verify(VerifyIdentityRequest(
                details.dni, details.names, details.lastNames, details.birthDate.toString()
            ))
            if (response.isSuccessful) {
                when (response.body()?.verified) {
                    true -> IdentityVerificationResult.Verified(VerifiedIdentity(details))
                    false -> IdentityVerificationResult.Mismatch
                    null -> IdentityVerificationResult.UnexpectedError
                }
            } else {
                val code = response.errorBody()?.use { body ->
                    runCatching { gson.fromJson(body.string(), IdentityApiError::class.java)?.code }.getOrNull()
                }
                when {
                    response.code() == 422 && code == "IAM_IDENTITY_MISMATCH" -> IdentityVerificationResult.Mismatch
                    response.code() in 500..599 || response.code() == 408 || response.code() == 429 -> IdentityVerificationResult.Unavailable
                    response.code() == 400 -> IdentityVerificationResult.InvalidRequest
                    else -> IdentityVerificationResult.UnexpectedError
                }
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: MalformedJsonException) {
            IdentityVerificationResult.UnexpectedError
        } catch (_: IOException) {
            IdentityVerificationResult.Unavailable
        } catch (_: JsonParseException) {
            IdentityVerificationResult.UnexpectedError
        }
    }
}
