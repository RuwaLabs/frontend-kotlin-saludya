package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor() : ConfigurationRepository {

    override suspend fun getRules(): List<ConfigurationRule> = listOf(
        ConfigurationRule(
            id = "capacity_per_block",
            type = RuleType.CAPACITY,
            title = "Capacidad por bloque",
            value = RuleValue(value = "8", unit = "pacientes"),
        ),
        ConfigurationRule(
            id = "arrival_tolerance",
            type = RuleType.ARRIVAL_TOLERANCE,
            title = "Tolerancia de llegada",
            value = RuleValue(value = "10", unit = "minutos"),
        ),
        ConfigurationRule(
            id = "call_deadline",
            type = RuleType.CALL_DEADLINE,
            title = "Plazo después de llamada",
            value = RuleValue(value = "5", unit = "minutos"),
        ),
        ConfigurationRule(
            id = "resignation_response",
            type = RuleType.RESIGNATION_RESPONSE,
            title = "Respuesta a resignación",
            value = RuleValue(value = "5", unit = "minutos"),
        ),
    )

    override suspend fun getAdditionalRules(): List<ConfigurationRule> = listOf(
        ConfigurationRule(
            id = "reservation_limit",
            type = RuleType.RESERVATION_LIMIT,
            title = "Hora límite para reservar",
            value = RuleValue(value = "18:00", unit = ""),
        ),
        ConfigurationRule(
            id = "cancellation_anticipation",
            type = RuleType.CANCELLATION_ANTICIPATION,
            title = "Anticipación para cancelar",
            value = RuleValue(value = "24", unit = "horas"),
        ),
        ConfigurationRule(
            id = "resignation_priority",
            type = RuleType.RESIGNATION_PRIORITY,
            title = "Prioridad de resignación",
            value = RuleValue(value = "Por especialidad", unit = ""),
        ),
        ConfigurationRule(
            id = "queue_visibility",
            type = RuleType.QUEUE_VISIBILITY,
            title = "Posición de cola visible",
            value = RuleValue(value = "Sí", unit = ""),
        ),
    )
}