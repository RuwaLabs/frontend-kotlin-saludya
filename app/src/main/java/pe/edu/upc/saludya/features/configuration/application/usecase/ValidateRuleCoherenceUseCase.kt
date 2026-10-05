package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationResult
import javax.inject.Inject

class ValidateRuleCoherenceUseCase @Inject constructor(
    private val repository: ConfigurationRepository,
) {
    suspend operator fun invoke(ruleId: String, newValue: String): ValidationResult {
        val trimmed = newValue.trim()

        // 1) No vacío
        if (trimmed.isEmpty()) {
            return ValidationResult.Invalid(
                "El valor no puede estar vacío. Ingresa un número antes de guardar."
            )
        }

        // 2) Debe ser numérico
        val numericValue = trimmed.toIntOrNull()
            ?: return ValidationResult.Invalid(
                "El valor debe ser un número. Revisa los campos antes de guardar."
            )

        // 3) Mayor que cero
        if (numericValue <= 0) {
            return ValidationResult.Invalid(
                "La capacidad debe ser mayor que cero. Revisa los campos antes de guardar."
            )
        }

        // 4) Coherencia con otras reglas
        val allRules = repository.getRules() + repository.getAdditionalRules()
        val currentRule = allRules.firstOrNull { it.id == ruleId }
            ?: return ValidationResult.Invalid("Regla no encontrada.")

        // Si es capacidad por bloque, validar que no choque con tolerancia de llegada + plazo
        if (currentRule.type == RuleType.CAPACITY) {
            val tolerance = allRules.firstOrNull { it.type == RuleType.ARRIVAL_TOLERANCE }
                ?.value?.value?.toIntOrNull() ?: 0
            val deadline = allRules.firstOrNull { it.type == RuleType.CALL_DEADLINE }
                ?.value?.value?.toIntOrNull() ?: 0

            // Ejemplo de regla: la capacidad no puede ser menor que la tolerancia
            if (numericValue < tolerance) {
                return ValidationResult.Invalid(
                    "La capacidad ($numericValue) no puede ser menor que la tolerancia de llegada ($tolerance minutos). " +
                            "La tolerancia de llegada no puede superar la duración del intervalo. Revisa los campos antes de guardar."
                )
            }

            // Regla: la capacidad no puede exceder 100 (regla operativa del establecimiento)
            if (numericValue > 100) {
                return ValidationResult.Invalid(
                    "La capacidad no puede exceder 100 pacientes por bloque. Revisa los campos antes de guardar."
                )
            }
        }

        return ValidationResult.Valid
    }
}