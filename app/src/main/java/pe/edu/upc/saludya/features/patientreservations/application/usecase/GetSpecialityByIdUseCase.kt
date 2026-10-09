package pe.edu.upc.saludya.features.patientreservations.application.usecase

import pe.edu.upc.saludya.features.patientreservations.domain.repositories.SpecialityRepository
import javax.inject.Inject

class GetSpecialityByIdUseCase @Inject constructor(
    private val repository: SpecialityRepository
) {
    suspend operator fun invoke(id: Int) = repository.getSpecialityById(id)
}
