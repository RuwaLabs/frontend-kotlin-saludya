package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import javax.inject.Inject

class LinkedMinorRepositoryImpl @Inject constructor() : LinkedMinorRepository {

    override suspend fun getLinkedMinors(): List<LinkedMinor> = listOf(
        LinkedMinor(id = "1", fullName = "Mateo Torres", age = 8),
        LinkedMinor(id = "2", fullName = "Sofía Torres", age = 12),
    )
}