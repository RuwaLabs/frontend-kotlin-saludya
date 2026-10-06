package pe.edu.upc.saludya.features.configuration.presentation.myData

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
import pe.edu.upc.saludya.features.configuration.domain.entity.Patient
import pe.edu.upc.saludya.features.configuration.presentation.components.EditableField
import pe.edu.upc.saludya.features.configuration.presentation.components.InfoNote
import pe.edu.upc.saludya.features.configuration.presentation.components.LabeledField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

@Composable
fun MyDataScreen(
    viewModel: MyDataViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditPhoneClick: () -> Unit = {},
    onEditEmailClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Mis datos",
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
                is MyDataUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MyDataUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is MyDataUiState.Success -> {
                    MyDataContent(
                        patient = state.patient,
                        onEditPhoneClick = onEditPhoneClick,
                        onEditEmailClick = onEditEmailClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun MyDataContent(
    patient: Patient,
    onEditPhoneClick: () -> Unit,
    onEditEmailClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        LabeledField(
            label = "Nombre",
            value = patient.fullName,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LabeledField(
            label = "Documento",
            value = "${patient.documentType} ${patient.documentNumberMasked}",
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditableField(
            label = "Celular",
            value = patient.phoneNumber,
            onClick = onEditPhoneClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        EditableField(
            label = "Correo",
            value = patient.email,
            onClick = onEditEmailClick,
        )

        Spacer(modifier = Modifier.height(8.dp))

        InfoNote(
            text = "Los datos de identidad verificados se muestran para consulta.",
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "MyDataScreen")
@Composable
private fun MyDataScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        MyDataContent(
            patient = Patient(
                id = "1",
                fullName = "Lucía Torres",
                role = "Titular",
                documentType = "DNI",
                documentNumberMasked = "••••4821",
                phoneNumber = "987 654 482",
                email = "lucia.torres@correo.com",
            ),
            onEditPhoneClick = {},
            onEditEmailClick = {},
        )
    }
}