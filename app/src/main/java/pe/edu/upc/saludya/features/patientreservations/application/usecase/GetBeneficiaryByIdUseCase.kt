package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.BeneficiaryRepository
import javax.inject.Inject

class GetBeneficiaryByIdUseCase @Inject constructor(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(patientId: Int) = repository.getBeneficiaryById(patientId)
}
