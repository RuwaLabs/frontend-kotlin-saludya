package pe.edu.upc.saludya.features.configuration.presentation.exportReport

import pe.edu.upc.saludya.features.configuration.domain.entity.ReportExport

sealed interface ExportReportUiState {
    data class Editing(
        val fromDate: String = "01/10/2026",
        val toDate: String = "06/10/2026",
        val specialty: String = "Todas las especialidades",
        val isExporting: Boolean = false,
    ) : ExportReportUiState

    /**
     * H11: el reporte está listo para descargar.
     * Se muestra el diálogo "Reporte listo".
     */
    data class Ready(val report: ReportExport) : ExportReportUiState

    /**
     * H12: el rango de fechas es inválido (fecha inicial > fecha final).
     * Se muestra el diálogo "Revisa el valor" (ValidationDialog).
     */
    data class InvalidRange(val message: String) : ExportReportUiState

    data class Error(val message: String) : ExportReportUiState
}