package pe.edu.upc.saludya.features.configuration.domain.repository

import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators
import pe.edu.upc.saludya.features.configuration.domain.entity.ReportExport
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemand
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat

interface ConfigurationRepository {
    suspend fun getRules(): List<ConfigurationRule>
    suspend fun getAdditionalRules(): List<ConfigurationRule>
    suspend fun getRuleById(id: String): ConfigurationRule?
    suspend fun updateRule(id: String, newValue: String): Boolean
    suspend fun getDailyIndicators(): DailyIndicators
    suspend fun getSpecialtyDemand(): SpecialtyDemand
    suspend fun exportReport(
        fromDate: String,
        toDate: String,
        specialty: String,
        format: ExportFormat,
    ): ReportExport
    suspend fun getAttentionInterval(): ConfigurationRule
}