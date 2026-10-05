package pe.edu.upc.saludya.features.configuration.domain.entity

import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat

data class ReportExport(
    val fromDate: String,
    val toDate: String,
    val specialty: String,
    val format: ExportFormat,
    val filePath: String,
    val fileName: String,
)