package pe.edu.upc.saludya.features.configuration.presentation.specialtyDemand

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemand
import pe.edu.upc.saludya.features.configuration.domain.entity.SpecialtyDemandItem
import pe.edu.upc.saludya.features.configuration.presentation.components.DemandBarCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

private val SubtitleColor = Color(0xFF56706D)

@Composable
fun SpecialtyDemandScreen(
    viewModel: SpecialtyDemandViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onExportClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Demanda",
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
                is SpecialtyDemandUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is SpecialtyDemandUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is SpecialtyDemandUiState.Success -> {
                    SpecialtyDemandContent(
                        demand = state.demand,
                        onExportClick = onExportClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecialtyDemandContent(
    demand: SpecialtyDemand,
    onExportClick: () -> Unit,
) {
    val maxReservations = demand.items.maxOfOrNull { it.reservations }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Subtítulo gris
        Text(
            text = "${demand.dateLabel} · Reservas: ${demand.totalReservations}",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = SubtitleColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cards con padding
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp),
        ) {
            demand.items.forEach { item ->
                DemandBarCard(
                    specialtyName = item.specialtyName,
                    reservations = item.reservations,
                    fraction = item.reservations.toFloat() / maxReservations.toFloat(),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botón sin padding externo (el componente ya tiene 20dp interno)
        OutlineActionButton(
            text = "Exportar datos",
            onClick = onExportClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "SpecialtyDemandScreen")
@Composable
private fun SpecialtyDemandScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        SpecialtyDemandContent(
            demand = SpecialtyDemand(
                dateLabel = "6 oct 2026",
                totalReservations = 48,
                items = listOf(
                    SpecialtyDemandItem("Medicina general", 24),
                    SpecialtyDemandItem("Pediatría", 12),
                    SpecialtyDemandItem("Odontología", 8),
                    SpecialtyDemandItem("Dermatología", 4),
                ),
            ),
            onExportClick = {},
        )
    }
}