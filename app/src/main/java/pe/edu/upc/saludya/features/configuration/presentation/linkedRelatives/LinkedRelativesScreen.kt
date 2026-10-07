package pe.edu.upc.saludya.features.configuration.presentation.linkedRelatives

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
import pe.edu.upc.saludya.features.configuration.presentation.components.LinkedMinorCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription

@Composable
fun LinkedRelativesScreen(
    viewModel: LinkedRelativesViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onViewMinorClick: (String) -> Unit = {},
    onAddMinorClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Parientes vinculados",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = 3,
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
                is LinkedRelativesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LinkedRelativesUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is LinkedRelativesUiState.Success -> {
                    LinkedRelativesContent(
                        minors = state.minors,
                        onViewMinorClick = onViewMinorClick,
                        onAddMinorClick = onAddMinorClick,
                        onSyncClick = onSyncClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkedRelativesContent(
    minors: List<LinkedMinor>,
    onViewMinorClick: (String) -> Unit,
    onAddMinorClick: () -> Unit,
    onSyncClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ScreenDescription(
            text = "Reserva y consulta citas de los menores que tienes vinculados.",
        )

        minors.forEach { minor ->
            LinkedMinorCard(
                fullName = minor.fullName,
                age = minor.age,
                onViewDetailsClick = { onViewMinorClick(minor.id) },
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            text = "Agregar menor vinculado",
            onClick = onAddMinorClick,
        )

        OutlineActionButton(
            text = "Sincronizar vínculos",
            onClick = onSyncClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedRelativesScreen")
@Composable
private fun LinkedRelativesScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedRelativesContent(
            minors = listOf(
                LinkedMinor(id = "1", fullName = "Mateo Torres", age = 8),
                LinkedMinor(id = "2", fullName = "Sofía Torres", age = 12),
            ),
            onViewMinorClick = {},
            onAddMinorClick = {},
            onSyncClick = {},
        )
    }
}