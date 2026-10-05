package pe.edu.upc.saludya.features.configuration.application.usecase

import pe.edu.upc.saludya.features.configuration.domain.entity.ReportExport
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat
import javax.inject.Inject

class ExportReportUseCase @Inject constructor(
    private val repository: ConfigurationRepository,
) {
    suspend operator fun invoke(
        fromDate: String,
        toDate: String,
        specialty: String,
        format: ExportFormat,
    ): ReportExport = repository.exportReport(fromDate, toDate, specialty, format)
}