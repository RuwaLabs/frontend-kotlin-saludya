package pe.edu.upc.saludya.features.configuration.domain.entity

import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue

data class ConfigurationRule(
    val id: String,
    val type: RuleType,
    val title: String,
    val value: RuleValue,
)