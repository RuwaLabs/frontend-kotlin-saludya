package pe.edu.upc.saludya.features.configuration.presentation.quickEditRule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.domain.entity.ConfigurationRule
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleType
import pe.edu.upc.saludya.features.configuration.domain.valueobject.RuleValue
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField

private val TitleColor = Color(0xFF181C1C)
private val SubtitleColor = Color(0xFF56706D)
private val LabelColor = Color(0xFF56706D)
private val ErrorColor = Color(0xFFBA1A1A)

@Composable
fun QuickEditRuleDialog(
    ruleId: String,
    onDismiss: () -> Unit,
    onSaved: () -> Unit,
    viewModel: QuickEditRuleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(ruleId) {
        viewModel.loadRule(ruleId)
    }

    LaunchedEffect(uiState) {
        if (uiState is QuickEditRuleUiState.Saved) {
            onSaved()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 0.dp,
        ) {
            when (val state = uiState) {
                is QuickEditRuleUiState.Loading -> {
                    // Cargando - pequeño placeholder
                    Spacer(modifier = Modifier.height(80.dp))
                }
                is QuickEditRuleUiState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.padding(24.dp),
                        color = ErrorColor,
                    )
                }
                is QuickEditRuleUiState.Saved -> {
                    Spacer(modifier = Modifier.height(80.dp))
                }
                is QuickEditRuleUiState.Editing -> {
                    QuickEditContent(
                        ruleTitle = state.rule.title,
                        currentValue = state.rule.value.formatted,
                        inputValue = state.inputValue,
                        validationError = state.validationError,
                        isSaving = state.isSaving,
                        onValueChange = viewModel::onValueChange,
                        onSave = viewModel::saveRule,
                        onCancel = onDismiss,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickEditContent(
    ruleTitle: String,
    currentValue: String,
    inputValue: String,
    validationError: String?,
    isSaving: Boolean,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text(
            text = "Ingrese el nuevo valor",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TitleColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = ruleTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = SubtitleColor,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Valor actual: $currentValue",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = SubtitleColor,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Nuevo valor",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = LabelColor,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = inputValue,
            onValueChange = onValueChange,
        )

        if (validationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = validationError,
                modifier = Modifier.padding(horizontal = 20.dp),
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = ErrorColor,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = if (isSaving) "Actualizando..." else "Actualizar",
            onClick = onSave,
            enabled = !isSaving,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlineActionButton(
            text = "Cancelar",
            onClick = onCancel,
        )
    }
}

@Preview(showBackground = true, name = "QuickEditRuleDialog")
@Composable
private fun QuickEditRuleDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                QuickEditContent(
                    ruleTitle = "Capacidad máxima por bloque",
                    currentValue = "8 pacientes",
                    inputValue = "10",
                    validationError = null,
                    isSaving = false,
                    onValueChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }
    }
}