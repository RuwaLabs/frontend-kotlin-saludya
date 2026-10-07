package pe.edu.upc.saludya.iam.domain.repository

import pe.edu.upc.saludya.iam.domain.model.RegistrationCredentials
import pe.edu.upc.saludya.iam.domain.model.VerifiedIdentity

data class RegistrationReceipt(val patientId: Long, val accountId: Long, val email: String)

sealed interface RegistrationResult {
    data object CodeSent : RegistrationResult
    data object InvalidCode : RegistrationResult
    data object ExpiredCode : RegistrationResult
    data object TooManyAttempts : RegistrationResult
    data class Accepted(val receipt: RegistrationReceipt) : RegistrationResult
    data object EmailInUse : RegistrationResult
    data object IdentityAlreadyRegistered : RegistrationResult
    data object IdentityMismatch : RegistrationResult
    data object AdultRequired : RegistrationResult
    data object DuplicateRecord : RegistrationResult
    data object InvalidRequest : RegistrationResult
    data object Unavailable : RegistrationResult
    data object UnexpectedError : RegistrationResult
}

interface RegistrationRepository {
    suspend fun sendCode(email: String): RegistrationResult
    suspend fun register(identity: VerifiedIdentity, credentials: RegistrationCredentials, code: String): RegistrationResult
}
