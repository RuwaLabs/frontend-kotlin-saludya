package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import javax.inject.Inject

class GetLinkedMinorByIdUseCase @Inject constructor(
    private val repository: LinkedMinorRepository,
) {
    suspend operator fun invoke(id: String): LinkedMinor? = repository.getLinkedMinorById(id)
}