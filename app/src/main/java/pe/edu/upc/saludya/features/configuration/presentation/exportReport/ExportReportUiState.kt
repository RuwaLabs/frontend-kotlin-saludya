package pe.edu.upc.saludya.features.configuration.presentation.exportReport

import pe.edu.upc.saludya.features.configuration.domain.entity.ReportExport
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat

sealed interface ExportReportUiState {
    data class Editing(
        val fromDate: String = "01/10/2026",
        val toDate: String = "06/10/2026",
        val specialty: String = "Todas las especialidades",
        val isExporting: Boolean = false,
    ) : ExportReportUiState
    data class Success(val report: ReportExport) : ExportReportUiState
    data class InvalidRange(val message: String) : ExportReportUiState
    data class Error(val message: String) : ExportReportUiState
}