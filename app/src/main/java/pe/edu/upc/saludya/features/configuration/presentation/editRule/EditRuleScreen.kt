package pe.edu.upc.saludya.features.configuration.presentation.editRule

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
import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import pe.edu.upc.saludya.features.configuration.presentation.components.InfoNote
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

private val TitleColor = Color(0xFF181C1C)
private val LabelColor = Color(0xFF56706D)

@Composable
fun EditRuleScreen(
    ruleId: String,
    viewModel: EditRuleViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    onSaved: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(ruleId) {
        viewModel.loadRule(ruleId)
    }

    LaunchedEffect(uiState) {
        if (uiState is EditRuleUiState.Saved) {
            onSaved()
        }
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Editar capacidad",
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
                is EditRuleUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EditRuleUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is EditRuleUiState.Saved -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is EditRuleUiState.Success -> {
                    EditRuleContent(
                        ruleTitle = state.rule.title,
                        inputValue = state.inputValue,
                        onValueChange = viewModel::onValueChange,
                        onSave = viewModel::saveRule,
                        onCancel = onCancelClick,
                        isSaving = state.isSaving,
                    )
                }
            }
        }
    }
}

@Composable
private fun EditRuleContent(
    ruleTitle: String,
    inputValue: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isSaving: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = ruleTitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TitleColor,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Número de pacientes",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = LabelColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = inputValue,
            onValueChange = onValueChange,
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoNote(
            text = "Debe ser un número mayor que cero. Los cambios se aplican según la configuración operativa y no eliminan reservas confirmadas.",
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = if (isSaving) "Guardando..." else "Guardar cambios",
            onClick = onSave,
            enabled = !isSaving,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlineActionButton(
            text = "Cancelar",
            onClick = onCancel,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "EditRuleScreen")
@Composable
private fun EditRuleScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        EditRuleContent(
            ruleTitle = "Capacidad máxima por bloque",
            inputValue = "8",
            onValueChange = {},
            onSave = {},
            onCancel = {},
            isSaving = false,
        )
    }
}