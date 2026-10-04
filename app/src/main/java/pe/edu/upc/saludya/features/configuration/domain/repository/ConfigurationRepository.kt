package pe.edu.upc.saludya.features.configuration.domain.repository

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule

interface ConfigurationRepository {
    suspend fun getRules(): List<ConfigurationRule>
}