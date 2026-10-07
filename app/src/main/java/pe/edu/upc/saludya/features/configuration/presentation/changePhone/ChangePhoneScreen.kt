package pe.edu.upc.saludya.features.configuration.presentation.changePhone

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.core.desingsystem.component.BottomNavigationBar
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationDialog

@Composable
fun ChangePhoneScreen(
    viewModel: ChangePhoneViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onCodeSent: (String) -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is ChangePhoneUiState.CodeSent) {
            onCodeSent(state.newPhone)
        }
    }

    val current = uiState

    // Modal: "Revisa el dato ingresado" (early return)
    if (current is ChangePhoneUiState.InvalidInput) {
        ValidationDialog(
            title = "Revisa el dato ingresado",
            message = current.message,
            buttonText = "Entendido",
            onCorrectClick = viewModel::dismissInvalidInput,
        )
        return
    }

    // Early return: Error (no debería llegar aquí, pero por seguridad)
    if (current is ChangePhoneUiState.Error) {
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
                title = "Cambiar celular",
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
        // Solo manejamos Editing aquí (los otros estados se manejan con early return)
        val state = uiState
        if (state is ChangePhoneUiState.Editing) {
            ChangePhoneContent(
                modifier = Modifier.padding(innerPadding),
                inputValue = state.newPhone,
                isSending = state.isSending,
                onValueChange = viewModel::onPhoneChange,
                onSendCode = viewModel::sendCode,
            )
        }
    }
}

@Composable
private fun ChangePhoneContent(
    modifier: Modifier = Modifier,
    inputValue: String,
    isSending: Boolean,
    onValueChange: (String) -> Unit,
    onSendCode: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScreenDescription(
            text = "Confirmarás el cambio con un código enviado a tu correo registrado. El celular es un dato de contacto.",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nuevo celular",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        RuleValueField(
            value = inputValue,
            onValueChange = onValueChange,
            placeholder = "987 654 482",
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (isSending) "Enviando..." else "Enviar código",
            onClick = onSendCode,
            enabled = !isSending,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "ChangePhoneScreen")
@Composable
private fun ChangePhoneScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ChangePhoneContent(
            inputValue = "987654482",
            isSending = false,
            onValueChange = {},
            onSendCode = {},
        )
    }
}