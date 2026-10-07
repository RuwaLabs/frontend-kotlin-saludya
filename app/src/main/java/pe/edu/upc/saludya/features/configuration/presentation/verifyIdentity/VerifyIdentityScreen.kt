package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationDialog

@Composable
fun VerifyIdentityScreen(
    viewModel: VerifyIdentityViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    val current = uiState

    // Modal: "Vinculación completada" (éxito)
    if (current is VerifyIdentityUiState.Success) {
        ValidationDialog(
            title = "Vinculación completada",
            message = "Ahora puedes reservar y consultar citas para Mateo Torres.",
            buttonText = "Entendido",
            onCorrectClick = onSuccess,
        )
        return
    }

    // Modal: "No pudimos validar el vínculo" (error)
    if (current is VerifyIdentityUiState.ValidationFailed) {
        ValidationDialog(
            title = "No pudimos validar el vínculo",
            message = current.message,
            buttonText = "Entendido",
            onCorrectClick = viewModel::dismissValidationFailed,
        )
        return
    }

    // Error inesperado
    if (current is VerifyIdentityUiState.Error) {
        Text(
            text = current.message,
            modifier = Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.error,
        )
        return
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Verificar identidad",
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
        when (val state = uiState) {
            is VerifyIdentityUiState.Editing -> {
                VerifyIdentityContent(
                    modifier = Modifier.padding(innerPadding),
                    minorName = state.minorName,
                    birthDate = state.birthDate,
                    isSubmitting = state.isSubmitting,
                    onMinorNameChange = viewModel::onMinorNameChange,
                    onBirthDateChange = viewModel::onBirthDateChange,
                    onSubmit = viewModel::submit,
                )
            }
            else -> {
                // Estados ya manejados con early return
            }
        }
    }
}

@Composable
private fun VerifyIdentityContent(
    modifier: Modifier = Modifier,
    minorName: String,
    birthDate: String,
    isSubmitting: Boolean,
    onMinorNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScreenDescription(
            text = "Completa los datos solicitados para continuar con la validación del vínculo.",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nombres del menor",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = minorName,
            onValueChange = onMinorNameChange,
            placeholder = "Mateo",
            keyboardType = KeyboardType.Text,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fecha de nacimiento",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = birthDate,
            onValueChange = onBirthDateChange,
            placeholder = "12/04/2018",
            keyboardType = KeyboardType.Number,
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSubmitting) "Verificando..." else "Continuar",
            onClick = onSubmit,
            enabled = !isSubmitting,
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoNote(
            text = "La vinculación requiere la validación correspondiente.",
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "VerifyIdentityScreen")
@Composable
private fun VerifyIdentityScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        VerifyIdentityContent(
            minorName = "Mateo",
            birthDate = "12/04/2018",
            isSubmitting = false,
            onMinorNameChange = {},
            onBirthDateChange = {},
            onSubmit = {},
        )
    }
}