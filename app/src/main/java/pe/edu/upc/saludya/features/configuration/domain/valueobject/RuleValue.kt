package pe.edu.upc.saludya.features.configuration.domain.valueobject

data class RuleValue(
    val value: String,
    val unit: String,
) {
    val formatted: String
        get() = if (unit.isBlank()) value else "$value $unit"
}