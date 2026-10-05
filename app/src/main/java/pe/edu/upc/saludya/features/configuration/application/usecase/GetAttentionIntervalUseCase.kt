package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import javax.inject.Inject

class GetAttentionIntervalUseCase @Inject constructor(
    private val repository: ConfigurationRepository,
) {
    suspend operator fun invoke(): ConfigurationRule = repository.getAttentionInterval()
}