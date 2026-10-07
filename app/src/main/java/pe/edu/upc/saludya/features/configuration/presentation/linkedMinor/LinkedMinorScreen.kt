package pe.edu.upc.saludya.features.configuration.presentation.linkedMinor

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
import pe.edu.upc.saludya.features.configuration.presentation.components.MinorDetailCard
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

@Composable
fun LinkedMinorScreen(
    minorId: String,
    viewModel: LinkedMinorViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onViewAppointmentsClick: () -> Unit = {},
    onSyncClick: () -> Unit = {},
    onUnlinkClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(minorId) {
        viewModel.loadMinor(minorId)
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Menor vinculado",
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
                is LinkedMinorUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is LinkedMinorUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is LinkedMinorUiState.Success -> {
                    LinkedMinorContent(
                        minor = state.minor,
                        onViewAppointmentsClick = onViewAppointmentsClick,
                        onSyncClick = onSyncClick,
                        onUnlinkClick = onUnlinkClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LinkedMinorContent(
    minor: LinkedMinor,
    onViewAppointmentsClick: () -> Unit,
    onSyncClick: () -> Unit,
    onUnlinkClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MinorDetailCard(
            fullName = minor.fullName,
            documentType = minor.documentType,
            documentNumberMasked = minor.documentNumberMasked,
            birthDate = minor.birthDate,
            representativeName = minor.representativeName,
            isVerified = minor.isVerified,
        )

        Spacer(modifier = Modifier.height(4.dp))

        PrimaryButton(
            text = "Ver sus citas",
            onClick = onViewAppointmentsClick,
        )

        OutlineActionButton(
            text = "Sincronizar datos",
            onClick = onSyncClick,
        )

        OutlineActionButton(
            text = "Dejar vinculación",
            onClick = onUnlinkClick,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "LinkedMinorScreen")
@Composable
private fun LinkedMinorScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        LinkedMinorContent(
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
            onSyncClick = {},
            onUnlinkClick = {},
        )
    }
}