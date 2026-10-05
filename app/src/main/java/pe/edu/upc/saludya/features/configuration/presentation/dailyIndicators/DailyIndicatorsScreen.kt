package pe.edu.upc.saludya.features.configuration.presentation.dailyIndicators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.DailyIndicators
import pe.edu.upc.saludya.features.configuration.presentation.components.AttentionSummaryCard
import pe.edu.upc.saludya.features.configuration.presentation.components.IndicatorCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

private val SubtitleColor = Color(0xFF56706D)

@Composable
fun DailyIndicatorsScreen(
    viewModel: DailyIndicatorsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onDemandClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Indicadores del día",
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
                is DailyIndicatorsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is DailyIndicatorsUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is DailyIndicatorsUiState.Success -> {
                    DailyIndicatorsContent(
                        indicators = state.indicators,
                        onDemandClick = onDemandClick,
                        onExportClick = onExportClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyIndicatorsContent(
    indicators: DailyIndicators,
    onDemandClick: () -> Unit,
    onExportClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Subtítulo gris
        Text(
            text = "${indicators.dateLabel} · ${indicators.centerName}",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = SubtitleColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid 2x2 - Fila 1: Programadas + Pendientes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                IndicatorCard(value = indicators.programmed, label = "Programadas")
            }
            Box(modifier = Modifier.weight(1f)) {
                IndicatorCard(value = indicators.pending, label = "Pendientes")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid 2x2 - Fila 2: Canceladas + Inasistencias
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                IndicatorCard(value = indicators.cancelled, label = "Canceladas")
            }
            Box(modifier = Modifier.weight(1f)) {
                IndicatorCard(value = indicators.noShow, label = "Inasistencias")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card resumen de atención
        Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            AttentionSummaryCard(
                title = "Resumen de atención",
                detailsLine = "${indicators.attendedAppointments} citas atendidas · ${indicators.recoveredSlots} cupos recuperados",
                updatedLine = indicators.lastUpdatedLabel,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botones SIN padding externo (el componente ya tiene 20dp interno)
        PrimaryButton(
            text = "Ver demanda por especialidad",
            onClick = onDemandClick,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlineActionButton(
            text = "Exportar reporte",
            onClick = onExportClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "DailyIndicatorsScreen")
@Composable
private fun DailyIndicatorsScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        DailyIndicatorsContent(
            indicators = DailyIndicators(
                dateLabel = "Mar 6 oct 2026",
                centerName = "Centro de Salud",
                programmed = 48,
                pending = 6,
                cancelled = 2,
                noShow = 4,
                attendedAppointments = 36,
                recoveredSlots = 6,
                lastUpdatedLabel = "Actualizado a las 16:30",
            ),
            onDemandClick = {},
            onExportClick = {},
        )
    }
}