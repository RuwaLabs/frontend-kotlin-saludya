package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemand
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemandItem
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor() : ConfigurationRepository {

    private val rules = mutableListOf(
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

    private val additionalRules = mutableListOf(
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

    override suspend fun getRules(): List<ConfigurationRule> = rules.toList()

    override suspend fun getAdditionalRules(): List<ConfigurationRule> = additionalRules.toList()

    override suspend fun getRuleById(id: String): ConfigurationRule? =
        (rules + additionalRules).firstOrNull { it.id == id }

    override suspend fun updateRule(id: String, newValue: String): Boolean {
        val all = rules + additionalRules
        val current = all.firstOrNull { it.id == id } ?: return false
        val updated = current.copy(value = current.value.copy(value = newValue))

        val idxMain = rules.indexOfFirst { it.id == id }
        if (idxMain >= 0) {
            rules[idxMain] = updated
            return true
        }
        val idxAdd = additionalRules.indexOfFirst { it.id == id }
        if (idxAdd >= 0) {
            additionalRules[idxAdd] = updated
            return true
        }
        return false
    }

    override suspend fun getDailyIndicators(): DailyIndicators = DailyIndicators(
        dateLabel = "Mar 6 oct 2026",
        centerName = "Centro de Salud",
        programmed = 48,
        pending = 6,
        cancelled = 2,
        noShow = 4,
        attendedAppointments = 36,
        recoveredSlots = 6,
        lastUpdatedLabel = "Actualizado a las 16:30",
    )

    override suspend fun getSpecialtyDemand(): SpecialtyDemand = SpecialtyDemand(
        dateLabel = "6 oct 2026",
        totalReservations = 48,
        items = listOf(
            SpecialtyDemandItem(specialtyName = "Medicina general", reservations = 24),
            SpecialtyDemandItem(specialtyName = "Pediatría", reservations = 12),
            SpecialtyDemandItem(specialtyName = "Odontología", reservations = 8),
            SpecialtyDemandItem(specialtyName = "Dermatología", reservations = 4),
        ),
    )
}