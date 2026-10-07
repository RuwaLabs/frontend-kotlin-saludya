package pe.edu.upc.saludya.iam.domain.repository

import java.time.Instant

enum class AccessFailure {
    INVALID_CREDENTIALS, INACTIVE_ACCOUNT, INVALID_CODE, EXPIRED_CODE, TOO_MANY_ATTEMPTS,
    NOT_PATIENT, INVALID_REQUEST, UNAVAILABLE, UNEXPECTED
}

sealed interface AccessResult<out T> {
    data class Success<T>(val value: T) : AccessResult<T>
    data class Failure(val reason: AccessFailure) : AccessResult<Nothing>
}

data class LoginChallenge(val id: String, val maskedEmail: String, val expiresAt: Instant)

data class PatientSession(
    val accessToken: String, val expiresAt: Instant, val userId: Long, val patientId: Long
) {
    override fun toString(): String = "PatientSession[REDACTED]"
}

interface AccessRepository {
    suspend fun login(email: String, password: String): AccessResult<LoginChallenge>
    suspend fun verifyLogin(challengeId: String, code: String): AccessResult<PatientSession>
    suspend fun resendLogin(challengeId: String): AccessResult<Unit>
    suspend fun recoverPassword(email: String): AccessResult<Unit>
    suspend fun logout(token: String): AccessResult<Unit>
}
