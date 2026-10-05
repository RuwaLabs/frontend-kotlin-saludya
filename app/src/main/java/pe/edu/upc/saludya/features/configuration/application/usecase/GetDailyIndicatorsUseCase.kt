package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import javax.inject.Inject

class GetDailyIndicatorsUseCase @Inject constructor(
    private val repository: ConfigurationRepository,
) {
    suspend operator fun invoke(): DailyIndicators = repository.getDailyIndicators()
}