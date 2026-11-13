package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.SpecialityRepository
import javax.inject.Inject

class GetAllSpecialitiesUseCase @Inject constructor(
    private val repository: SpecialityRepository
) {
    suspend operator fun invoke() = repository.getAllSpecialities()
}
