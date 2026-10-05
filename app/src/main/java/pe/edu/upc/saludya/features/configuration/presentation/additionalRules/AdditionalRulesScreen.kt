package pe.edu.upc.saludya.features.configuration.presentation.additionalRules

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
import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import pe.edu.upc.saludya.features.configuration.presentation.components.ConfigurationRuleCard
import pe.edu.upc.saludya.features.configuration.presentation.components.InfoNote
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar

@Composable
fun AdditionalRulesScreen(
    viewModel: AdditionalRulesViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditRuleClick: (String) -> Unit = {},
    onIntervalsClick: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Más reglas",
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
                is AdditionalRulesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is AdditionalRulesUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                is AdditionalRulesUiState.Success -> {
                    AdditionalRulesContent(
                        rules = state.rules,
                        onEditRuleClick = onEditRuleClick,
                        onIntervalsClick = onIntervalsClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun AdditionalRulesContent(
    rules: List<ConfigurationRule>,
    onEditRuleClick: (String) -> Unit,
    onIntervalsClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        rules.forEach { rule ->
            ConfigurationRuleCard(
                title = rule.title,
                value = rule.value.formatted,
                onEditClick = { onEditRuleClick(rule.id) },
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        InfoNote(
            text = "La prioridad de resignación sigue el orden de reserva. La atención sigue el orden de llegada.",
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlineActionButton(
            text = "Intervalos de atención",
            onClick = onIntervalsClick,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "AdditionalRulesScreen")
@Composable
private fun AdditionalRulesScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        AdditionalRulesContent(
            rules = listOf(
                ConfigurationRule(
                    id = "1",
                    type = RuleType.RESERVATION_LIMIT,
                    title = "Hora límite para reservar",
                    value = RuleValue("18:00", ""),
                ),
                ConfigurationRule(
                    id = "2",
                    type = RuleType.CANCELLATION_ANTICIPATION,
                    title = "Anticipación para cancelar",
                    value = RuleValue("24", "horas"),
                ),
                ConfigurationRule(
                    id = "3",
                    type = RuleType.RESIGNATION_PRIORITY,
                    title = "Prioridad de resignación",
                    value = RuleValue("Por especialidad", ""),
                ),
                ConfigurationRule(
                    id = "4",
                    type = RuleType.QUEUE_VISIBILITY,
                    title = "Posición de cola visible",
                    value = RuleValue("Sí", ""),
                ),
            ),
            onEditRuleClick = {},
            onIntervalsClick = {},
        )
    }
}