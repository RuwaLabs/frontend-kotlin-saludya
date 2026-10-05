package pe.edu.upc.saludya.features.configuration.infrastructure.repositories

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators
import pe.edu.upc.saludya.features.configuration.domain.entity.ReportExport
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemand
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemandItem
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import javax.inject.Inject

class ConfigurationRepositoryImpl @Inject constructor() : ConfigurationRepository {

    // Store único y mutable para todas las reglas del establecimiento.
    // Incluye reglas básicas, adicionales e intervalo de atención.
    private val rules = mutableListOf(
        // --- Reglas básicas (H01) ---
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

        // --- Reglas adicionales (H02) ---
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

        // --- Intervalos de atención (H13) ---
        ConfigurationRule(
            id = "attention_interval",
            type = RuleType.ATTENTION_INTERVAL,
            title = "Duración de cada intervalo",
            value = RuleValue(value = "30", unit = "minutos"),
        ),
    )

    override suspend fun getRules(): List<ConfigurationRule> =
        rules.filter { rule ->
            rule.type in listOf(
                RuleType.CAPACITY,
                RuleType.ARRIVAL_TOLERANCE,
                RuleType.CALL_DEADLINE,
                RuleType.RESIGNATION_RESPONSE,
            )
        }

    override suspend fun getAdditionalRules(): List<ConfigurationRule> =
        rules.filter { rule ->
            rule.type in listOf(
                RuleType.RESERVATION_LIMIT,
                RuleType.CANCELLATION_ANTICIPATION,
                RuleType.RESIGNATION_PRIORITY,
                RuleType.QUEUE_VISIBILITY,
            )
        }

    override suspend fun getRuleById(id: String): ConfigurationRule? =
        rules.firstOrNull { it.id == id }

    override suspend fun updateRule(id: String, newValue: String): Boolean {
        val index = rules.indexOfFirst { it.id == id }
        if (index < 0) return false
        val current = rules[index]
        rules[index] = current.copy(value = current.value.copy(value = newValue))
        return true
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

    override suspend fun exportReport(
        fromDate: String,
        toDate: String,
        specialty: String,
        format: ExportFormat,
    ): ReportExport {
        val ext = if (format == ExportFormat.PDF) "pdf" else "csv"
        val fileName = "reporte_${fromDate.replace("/", "")}_${toDate.replace("/", "")}.$ext"
        return ReportExport(
            fromDate = fromDate,
            toDate = toDate,
            specialty = specialty,
            format = format,
            filePath = "/storage/emulated/0/Downloads/$fileName",
            fileName = fileName,
        )
    }

    override suspend fun getAttentionInterval(): ConfigurationRule =
        rules.first { it.type == RuleType.ATTENTION_INTERVAL }
}