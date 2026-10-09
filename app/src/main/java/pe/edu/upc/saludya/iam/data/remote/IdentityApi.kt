package pe.edu.upc.saludya.iam.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class VerifyIdentityRequest(
    val dni: String,
    val name: String,
    val lastname: String,
    val birthDate: String
)

// Nullable: an empty/malformed response must never grant verification.
data class VerifyIdentityResponse(val verified: Boolean? = null)
data class IdentityApiError(val code: String? = null)

interface IdentityApi {
    @POST("api/v1/identity-verifications")
    suspend fun verify(@Body request: VerifyIdentityRequest): Response<VerifyIdentityResponse>
}
