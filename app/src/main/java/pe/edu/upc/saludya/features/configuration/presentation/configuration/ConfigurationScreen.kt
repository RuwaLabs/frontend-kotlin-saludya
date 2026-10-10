package pe.edu.upc.saludya.features.configuration.presentation.configuration

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PatientInfoCard
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

@Composable
fun ConfigurationScreen(
    viewModel: ConfigurationViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onMyDataClick: () -> Unit = {},
    onLinkedRelativesClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = stringResource(R.string.config_title),
                onBackClick = onBackClick,
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
                is ConfigurationUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ConfigurationUiState.Error -> {
                    Text(
                        text = stringResource(state.messageRes),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is ConfigurationUiState.Success -> {
                    ConfigurationContent(
                        patient = state.patient,
                        onMyDataClick = onMyDataClick,
                        onLinkedRelativesClick = onLinkedRelativesClick,
                        onChangePasswordClick = onChangePasswordClick,
                        onLogoutClick = onLogoutClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfigurationContent(
    patient: Patient,
    onMyDataClick: () -> Unit,
    onLinkedRelativesClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        PatientInfoCard(
            fullName = patient.fullName,
            role = patient.role,
            documentType = patient.documentType,
            documentNumberMasked = patient.documentNumberMasked,
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlineActionButton(
            text = stringResource(R.string.config_my_data),
            onClick = onMyDataClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.config_linked_relatives),
            onClick = onLinkedRelativesClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.config_change_password),
            onClick = onChangePasswordClick,
        )

        OutlineActionButton(
            text = stringResource(R.string.config_logout),
            onClick = onLogoutClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "ConfigurationScreen")
@Composable
private fun ConfigurationScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ConfigurationContent(
            patient = Patient(
                id = "1",
                fullName = "Lucía Torres",
                role = "Titular",
                documentType = "DNI",
                documentNumberMasked = "••••4821",
                phoneNumber = "987 654 482",
                email = "lucia.torres@correo.com",
            ),
            onMyDataClick = {},
            onLinkedRelativesClick = {},
            onChangePasswordClick = {},
            onLogoutClick = {},
        )
    }
}