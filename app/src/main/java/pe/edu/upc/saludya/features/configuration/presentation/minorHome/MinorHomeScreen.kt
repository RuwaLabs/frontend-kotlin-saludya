package pe.edu.upc.saludya.features.configuration.presentation.minorHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.LinkedMinor
import pe.edu.upc.saludya.features.configuration.presentation.components.AppointmentCard
import pe.edu.upc.saludya.features.configuration.presentation.components.HistoryCard
import pe.edu.upc.saludya.features.configuration.presentation.components.MinorHeader
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton

@Composable
fun MinorHomeScreen(
    minorId: String,
    viewModel: MinorHomeViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onViewAppointmentsClick: () -> Unit = {},
    onViewHistoryClick: () -> Unit = {},
    onBackToProfileClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(minorId) {
        viewModel.loadMinor(minorId)
    }

    Scaffold(
        topBar = {},
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
                is MinorHomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MinorHomeUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is MinorHomeUiState.Success -> {
                    MinorHomeContent(
                        minor = state.minor,
                        onViewAppointmentsClick = onViewAppointmentsClick,
                        onViewHistoryClick = onViewHistoryClick,
                        onBackToProfileClick = onBackToProfileClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MinorHomeContent(
    minor: LinkedMinor,
    onViewAppointmentsClick: () -> Unit,
    onViewHistoryClick: () -> Unit,
    onBackToProfileClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MinorHeader(
            greeting = "Hola, ${minor.fullName.split(" ").first()}",
            representativeName = minor.representativeName,
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppointmentCard(
            specialty = "Pediatría",
            patientName = minor.fullName,
            dateTime = "Mié 7 oct · 10:00–10:30",
            doctorName = "Dra. Carla Díaz",
            status = "Confirmada",
        )

        PrimaryButton(
            text = "Ver sus citas",
            onClick = onViewAppointmentsClick,
        )

        HistoryCard(
            title = "Historial de ${minor.fullName.split(" ").first()}",
            subtitle = "Consulta sus atenciones anteriores.",
            onViewHistoryClick = onViewHistoryClick,
        )

        OutlineActionButton(
            text = "Volver a mi perfil",
            onClick = onBackToProfileClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "MinorHomeScreen")
@Composable
private fun MinorHomeScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        MinorHomeContent(
            minor = LinkedMinor(
                id = "1",
                fullName = "Mateo Torres",
                age = 8,
                documentType = "DNI",
                documentNumberMasked = "••••2716",
                birthDate = "12 abr 2018",
                representativeName = "Lucía Torres",
                isVerified = true,
            ),
            onViewAppointmentsClick = {},
            onViewHistoryClick = {},
            onBackToProfileClick = {},
        )
    }
}