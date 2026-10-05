package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import javax.inject.Inject

class UpdateRuleUseCase @Inject constructor(
    private val repository: ConfigurationRepository,
) {
    suspend operator fun invoke(id: String, newValue: String): Boolean =
        repository.updateRule(id, newValue)
}