package pe.edu.upc.saludya.features.configuration.presentation.exportReport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.configuration.application.usecase.ExportReportUseCase
import pe.edu.upc.saludya.features.configuration.domain.valueobject.ExportFormat
import javax.inject.Inject

@HiltViewModel
class ExportReportViewModel @Inject constructor(
    private val exportReportUseCase: ExportReportUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExportReportUiState>(ExportReportUiState.Editing())
    val uiState: StateFlow<ExportReportUiState> = _uiState.asStateFlow()

    fun onFromDateChange(value: String) {
        val current = _uiState.value
        if (current is ExportReportUiState.Editing) {
            _uiState.value = current.copy(fromDate = value)
        }
    }

    fun onToDateChange(value: String) {
        val current = _uiState.value
        if (current is ExportReportUiState.Editing) {
            _uiState.value = current.copy(toDate = value)
        }
    }

    fun onSpecialtyChange(value: String) {
        val current = _uiState.value
        if (current is ExportReportUiState.Editing) {
            _uiState.value = current.copy(specialty = value)
        }
    }

    fun export(format: ExportFormat) {
        val current = _uiState.value
        if (current !is ExportReportUiState.Editing) return

        // Validar rango (H12)
        if (!isDateRangeValid(current.fromDate, current.toDate)) {
            _uiState.value = ExportReportUiState.InvalidRange(
                "La fecha inicial no puede ser posterior a la fecha final.",
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isExporting = true)
            runCatching {
                exportReportUseCase(
                    fromDate = current.fromDate,
                    toDate = current.toDate,
                    specialty = current.specialty,
                    format = format,
                )
            }
                .onSuccess { _uiState.value = ExportReportUiState.Success(it) }
                .onFailure {
                    _uiState.value = ExportReportUiState.Error(it.message ?: "Error")
                }
        }
    }

    fun dismissInvalidRange() {
        _uiState.value = ExportReportUiState.Editing()
    }

    fun dismissSuccess() {
        _uiState.value = ExportReportUiState.Editing()
    }

    private fun isDateRangeValid(from: String, to: String): Boolean {
        // Formato: dd/MM/yyyy
        val fromParts = from.split("/").mapNotNull { it.toIntOrNull() }
        val toParts = to.split("/").mapNotNull { it.toIntOrNull() }
        if (fromParts.size != 3 || toParts.size != 3) return false
        val fromDate = fromParts[2] * 10000 + fromParts[1] * 100 + fromParts[0]
        val toDate = toParts[2] * 10000 + toParts[1] * 100 + toParts[0]
        return fromDate <= toDate
    }
}