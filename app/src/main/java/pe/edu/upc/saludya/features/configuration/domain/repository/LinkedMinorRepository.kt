package pe.edu.upc.saludya.features.configuration.domain.repository

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

interface LinkedMinorRepository {
    suspend fun getLinkedMinors(): List<LinkedMinor>
}