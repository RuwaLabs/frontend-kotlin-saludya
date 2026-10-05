package pe.edu.upc.saludya.features.configuration.presentation.attentionIntervals

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.presentation.components.InfoNote
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

private val LabelColor = Color(0xFF56706D)

@Composable
fun AttentionIntervalsScreen(
    viewModel: AttentionIntervalsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is AttentionIntervalsUiState.Saved) {
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Intervalos de atención",
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
                is AttentionIntervalsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is AttentionIntervalsUiState.Saved -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is AttentionIntervalsUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is AttentionIntervalsUiState.Editing -> {
                    AttentionIntervalsContent(
                        inputValue = state.inputValue,
                        isSaving = state.isSaving,
                        onValueChange = viewModel::onValueChange,
                        onSave = viewModel::saveInterval,
                    )
                }
            }
        }
    }
}

@Composable
private fun AttentionIntervalsContent(
    inputValue: String,
    isSaving: Boolean,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Label
        Text(
            text = "Duración de cada intervalo",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = LabelColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo con sufijo "minutos" fijo (el valor interno es solo el número)
        RuleValueField(
            value = inputValue,
            onValueChange = onValueChange,
            suffix = "minutos",
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Nota intermedia
        InfoNote(
            text = "La tolerancia de llegada debe ser coherente con la duración del intervalo.",
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Botón
        PrimaryButton(
            text = if (isSaving) "Guardando..." else "Guardar intervalo",
            onClick = onSave,
            enabled = !isSaving,
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Nota final
        InfoNote(
            text = "La regla se aplicará a nuevos bloques. Se conservan las citas confirmadas.",
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "AttentionIntervalsScreen")
@Composable
private fun AttentionIntervalsScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        AttentionIntervalsContent(
            inputValue = "30",
            isSaving = false,
            onValueChange = {},
            onSave = {},
        )
    }
}