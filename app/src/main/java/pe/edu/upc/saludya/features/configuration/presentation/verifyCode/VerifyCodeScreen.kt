package pe.edu.upc.saludya.features.configuration.presentation.verifyCode

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
import pe.edu.upc.saludya.features.configuration.presentation.components.CodeInputField
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationDialog

@Composable
fun VerifyCodeScreen(
    descriptionText: String,
    viewModel: VerifyCodeViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onVerified: () -> Unit = {},
    onBottomNavSelected: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is VerifyCodeUiState.Verified) {
            onVerified()
        }
    }

    val current = uiState

    // Modal: "Código incorrecto"
    if (current is VerifyCodeUiState.WrongCode) {
        ValidationDialog(
            title = "Código incorrecto",
            message = current.message,
            buttonText = "Entendido",
            onCorrectClick = viewModel::dismissWrongCode,
        )
        return
    }

    // Modal: "El código venció"
    if (current is VerifyCodeUiState.CodeExpired) {
        ValidationDialog(
            title = "El código venció",
            message = current.message,
            buttonText = "Entendido",
            onCorrectClick = viewModel::dismissCodeExpired,
        )
        return
    }

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = "Verificar correo",
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
            is VerifyCodeUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(24.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }
            is VerifyCodeUiState.Verified -> {
                // Manejado por el LaunchedEffect
            }
            is VerifyCodeUiState.WrongCode -> {
                // Manejado por el early return
            }
            is VerifyCodeUiState.CodeExpired -> {
                // Manejado por el early return
            }
            is VerifyCodeUiState.Editing -> {
                VerifyCodeContent(
                    modifier = Modifier.padding(innerPadding),
                    descriptionText = descriptionText,
                    code = state.code,
                    secondsRemaining = state.secondsRemaining,
                    error = state.error,
                    isVerifying = state.isVerifying,
                    onCodeChange = viewModel::onCodeChange,
                    onVerify = viewModel::verifyCode,
                    onResend = viewModel::resendCode,
                )
            }
        }
    }
}

@Composable
private fun VerifyCodeContent(
    modifier: Modifier = Modifier,
    descriptionText: String,
    code: String,
    secondsRemaining: Int,
    error: String?,
    isVerifying: Boolean,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        ScreenDescription(text = descriptionText)

        Spacer(modifier = Modifier.height(20.dp))

        CodeInputField(
            code = code,
            onCodeChange = onCodeChange,
        )

        Spacer(modifier = Modifier.height(12.dp))

        val secondsFormatted = secondsRemaining.toString().padStart(2, '0')
        Text(
            text = "Puedes solicitar otro código en 00:$secondsFormatted",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                modifier = Modifier.padding(horizontal = 20.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = if (isVerifying) "Verificando..." else "Verificar código",
            onClick = onVerify,
            enabled = !isVerifying,
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlineActionButton(
            text = "Reenviar código",
            onClick = onResend,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "VerifyCodeScreen")
@Composable
private fun VerifyCodeScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        VerifyCodeContent(
            descriptionText = "Ingresa el código de 6 dígitos enviado a l••••a@correo.com.",
            code = "241860",
            secondsRemaining = 45,
            error = null,
            isVerifying = false,
            onCodeChange = {},
            onVerify = {},
            onResend = {},
        )
    }
}