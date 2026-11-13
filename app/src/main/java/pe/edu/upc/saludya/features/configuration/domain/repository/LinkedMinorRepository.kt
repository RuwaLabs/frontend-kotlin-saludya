package pe.edu.upc.saludya.features.configuration.domain.repository

import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor

interface LinkedMinorRepository {
    suspend fun getLinkedMinors(): List<LinkedMinor>
    suspend fun getLinkedMinorById(id: String): LinkedMinor?
    suspend fun unlink(id: String)
    suspend fun linkMinor(dni: String, name: String, lastname: String, birthDate: String): String
}
