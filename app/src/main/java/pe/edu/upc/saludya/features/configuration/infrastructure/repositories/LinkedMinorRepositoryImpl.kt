package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import javax.inject.Inject

class LinkedMinorRepositoryImpl @Inject constructor() : LinkedMinorRepository {

    private val minors = listOf(
        LinkedMinor(
            id = "1",
            fullName = "Mateo Torres",
            age = 8,
            documentType = "DNI",
            documentNumberMasked = "••••2716",
            birthDate = "12 abr 2018",
            representativeName = "Lucía Torres",
            isVerified = true,
        ),
        LinkedMinor(
            id = "2",
            fullName = "Sofía Torres",
            age = 12,
            documentType = "DNI",
            documentNumberMasked = "••••5512",
            birthDate = "5 ene 2014",
            representativeName = "Lucía Torres",
            isVerified = true,
        ),
    )

    override suspend fun getLinkedMinors(): List<LinkedMinor> = minors

    override suspend fun getLinkedMinorById(id: String): LinkedMinor? =
        minors.firstOrNull { it.id == id }
}