package pe.edu.upc.saludya.features.configuration.presentation.configuration

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import pe.edu.upc.saludya.features.configuration.presentation.components.ConfigurationRuleCard
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.SectionTitle
import pe.edu.upc.saludya.features.configuration.presentation.quickEditRule.QuickEditRuleDialog

@Composable
fun ConfigurationScreen(
    viewModel: ConfigurationViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditRuleClick: (String) -> Unit = {},
    onSeeMoreRulesClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var editingRuleId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Configuración general",
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
                is ConfigurationUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ConfigurationUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is ConfigurationUiState.Success -> {
                    ConfigurationContent(
                        rules = state.rules,
                        onEditRuleClick = { ruleId ->
                            editingRuleId = ruleId
                            onEditRuleClick(ruleId)
                        },
                        onSeeMoreRulesClick = onSeeMoreRulesClick,
                    )
                }
            }
        }

        val currentEditing = editingRuleId
        if (currentEditing != null) {
            QuickEditRuleDialog(
                ruleId = currentEditing,
                onDismiss = { editingRuleId = null },
                onSaved = {
                    editingRuleId = null
                    viewModel.loadRules()
                },
            )
        }
    }
}

@Composable
private fun ConfigurationContent(
    rules: List<ConfigurationRule>,
    onEditRuleClick: (String) -> Unit,
    onSeeMoreRulesClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        SectionTitle(text = "Reglas del establecimiento")

        rules.forEach { rule ->
            ConfigurationRuleCard(
                title = rule.title,
                value = rule.value.formatted,
                onEditClick = { onEditRuleClick(rule.id) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onSeeMoreRulesClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
            ),
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Text(
                text = "Ver más reglas",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "ConfigurationScreen")
@Composable
private fun ConfigurationScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ConfigurationContent(
            rules = listOf(
                ConfigurationRule(
                    id = "1",
                    type = RuleType.CAPACITY,
                    title = "Capacidad por bloque",
                    value = RuleValue("8", "pacientes"),
                ),
                ConfigurationRule(
                    id = "2",
                    type = RuleType.ARRIVAL_TOLERANCE,
                    title = "Tolerancia de llegada",
                    value = RuleValue("10", "minutos"),
                ),
                ConfigurationRule(
                    id = "3",
                    type = RuleType.CALL_DEADLINE,
                    title = "Plazo después de llamada",
                    value = RuleValue("5", "minutos"),
                ),
                ConfigurationRule(
                    id = "4",
                    type = RuleType.RESIGNATION_RESPONSE,
                    title = "Respuesta a resignación",
                    value = RuleValue("5", "minutos"),
                ),
            ),
            onEditRuleClick = {},
            onSeeMoreRulesClick = {},
        )
    }
}