package pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories

import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.patientreservations.domain.model.Beneficiary
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.BeneficiaryRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.mapper.toDomain
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import javax.inject.Inject

class BeneficiaryRepositoryImpl @Inject constructor(
    private val service: PatientService,
    private val sessionProvider: SessionProvider
) : BeneficiaryRepository {

    override suspend fun getBeneficiaries(): List<Beneficiary> {
        val titularId = sessionProvider.patientId ?: return emptyList()
        val titular = runCatching { fetchPatient(titularId, titular = true) }.getOrNull()

        val minors = runCatching {
            val response = service.getMinors(titularId)
            if (!response.isSuccessful) return@runCatching emptyList()
            response.body().orEmpty()
        }.getOrDefault(emptyList())
            .mapNotNull { link ->
                runCatching { fetchPatient(link.patientId, titular = false) }.getOrNull()
            }

        return listOfNotNull(titular) + minors
    }

    override suspend fun getBeneficiaryById(patientId: Int): Beneficiary? {
        val currentPatientId = sessionProvider.patientId ?: return null
        val titular = patientId == currentPatientId
        return runCatching { fetchPatient(patientId, titular) }.getOrNull()
    }

    private suspend fun fetchPatient(patientId: Int, titular: Boolean): Beneficiary? {
        val response = service.getPatient(patientId)
        val dto = response.body() ?: return null
        return dto.toDomain(titular)
    }
}
