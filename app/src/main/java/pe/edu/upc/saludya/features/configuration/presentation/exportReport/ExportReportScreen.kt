package pe.edu.upc.saludya.features.configuration.presentation.exportReport

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat
import pe.edu.upc.saludya.features.configuration.presentation.components.InfoNote
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.SpecialtyDropdown
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationDialog

private val LabelColor = Color(0xFF56706D)

private val SpecialtyOptions = listOf(
    "Todas las especialidades",
    "Medicina general",
    "Pediatría",
    "Odontología",
    "Dermatología",
)

@Composable
fun ExportReportScreen(
    viewModel: ExportReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onReportReady: (String) -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is ExportReportUiState.Success) {
            onReportReady(state.report.fileName)
            viewModel.dismissSuccess()
        }
    }

    // H12: modal de rango inválido
    val current = uiState
    if (current is ExportReportUiState.InvalidRange) {
        ValidationDialog(
            message = current.message,
            onCorrectClick = viewModel::dismissInvalidRange,
        )
        return
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Exportar reporte",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 0,
                onItemSelected = onBottomNavSelected,
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (val state = uiState) {
                is ExportReportUiState.Editing -> {
                    ExportReportContent(
                        fromDate = state.fromDate,
                        toDate = state.toDate,
                        specialty = state.specialty,
                        isExporting = state.isExporting,
                        onFromDateChange = viewModel::onFromDateChange,
                        onToDateChange = viewModel::onToDateChange,
                        onSpecialtyChange = viewModel::onSpecialtyChange,
                        onPdfClick = { viewModel.export(ExportFormat.PDF) },
                        onCsvClick = { viewModel.export(ExportFormat.CSV) },
                    )
                }
                is ExportReportUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(24.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is ExportReportUiState.Success -> {
                    // Cierre manejado por LaunchedEffect
                }
                is ExportReportUiState.InvalidRange -> {
                    // Cierre manejado por el early return
                }
            }
        }
    }
}

@Composable
private fun ExportReportContent(
    fromDate: String,
    toDate: String,
    specialty: String,
    isExporting: Boolean,
    onFromDateChange: (String) -> Unit,
    onToDateChange: (String) -> Unit,
    onSpecialtyChange: (String) -> Unit,
    onPdfClick: () -> Unit,
    onCsvClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Desde
        Text(
            text = "Desde",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RuleValueField(
            value = fromDate,
            onValueChange = onFromDateChange,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hasta
        Text(
            text = "Hasta",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RuleValueField(
            value = toDate,
            onValueChange = onToDateChange,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Especialidad
        Text(
            text = "Especialidad",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelColor,
        )
        Spacer(modifier = Modifier.height(8.dp))
        SpecialtyDropdown(
            selected = specialty,
            options = SpecialtyOptions,
            onSelected = onSpecialtyChange,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botones
        PrimaryButton(
            text = if (isExporting) "Descargando..." else "Descargar PDF",
            onClick = onPdfClick,
            enabled = !isExporting,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlineActionButton(
            text = "Descargar CSV",
            onClick = onCsvClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nota
        InfoNote(
            text = "Incluye reservas, atenciones, ausencias, demanda y resignaciones del período.",
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "ExportReportScreen")
@Composable
private fun ExportReportScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ExportReportContent(
            fromDate = "01/10/2026",
            toDate = "06/10/2026",
            specialty = "Todas las especialidades",
            isExporting = false,
            onFromDateChange = {},
            onToDateChange = {},
            onSpecialtyChange = {},
            onPdfClick = {},
            onCsvClick = {},
        )
    }
}