package pe.edu.upc.saludya.features.iam.domain.repository

import pe.edu.upc.saludya.features.iam.domain.model.IdentityDetails
import pe.edu.upc.saludya.features.iam.domain.model.VerifiedIdentity

sealed interface IdentityVerificationResult {
    data class Verified(val identity: VerifiedIdentity) : IdentityVerificationResult
    data object Mismatch : IdentityVerificationResult
    data object Unavailable : IdentityVerificationResult
    data object InvalidRequest : IdentityVerificationResult
    data object UnexpectedError : IdentityVerificationResult
}

interface IdentityRepository {
    suspend fun verify(details: IdentityDetails): IdentityVerificationResult
}
