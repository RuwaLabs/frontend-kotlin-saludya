package pe.edu.upc.saludya.features.patientreservations.domain.repositories

import pe.edu.upc.saludya.features.patientreservations.domain.model.Beneficiary

interface BeneficiaryRepository {
    suspend fun getBeneficiaries(): List<Beneficiary>
    suspend fun getBeneficiaryById(patientId: Int): Beneficiary?
}
