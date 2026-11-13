package pe.edu.upc.saludya.features.configuration.presentation.changePassword

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.configuration.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.configuration.presentation.components.RuleValueField
import pe.edu.upc.saludya.features.configuration.presentation.components.SaludYaTopBar
import pe.edu.upc.saludya.features.configuration.presentation.components.ScreenDescription
import pe.edu.upc.saludya.features.configuration.presentation.validation.ValidationDialog

@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit = {},
    onSessionEnded: () -> Unit = {},
    viewModel: ChangePasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SaludYaTopBar(
                title = stringResource(R.string.change_password_title),
                onBackClick = onBackClick,
            )
        },
        containerColor = Color.White,
    ) { innerPadding ->
        when (state.step) {
            ChangePasswordStep.LOADING -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Spacer(Modifier.height(48.dp))
                CircularProgressIndicator(modifier = Modifier.padding(24.dp))
            }

            ChangePasswordStep.ERROR -> ChangePasswordColumn(innerPadding) {
                Text(
                    text = state.error ?: stringResource(R.string.change_password_unavailable),
                    color = MaterialTheme.colorScheme.error,
                )
                PrimaryButton(
                    text = "Reintentar",
                    onClick = viewModel::loadEmail,
                )
            }

            ChangePasswordStep.READY -> ChangePasswordColumn(innerPadding) {
                ScreenDescription(text = stringResource(R.string.change_password_description))
                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge,
                )
                PrimaryButton(
                    text = stringResource(R.string.change_password_send_code),
                    enabled = !state.busy,
                    onClick = viewModel::sendCode,
                )
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }

            ChangePasswordStep.CODE_SENT -> ChangePasswordColumn(innerPadding) {
                Text(
                    text = state.message
                        ?: stringResource(R.string.change_password_code_sent),
                )
                RuleValueField(
                    value = state.code,
                    onValueChange = viewModel::editCode,
                    placeholder = stringResource(R.string.change_password_code_label),
                    keyboardType = KeyboardType.Number,
                )
                PrimaryButton(
                    text = stringResource(R.string.change_password_verify),
                    enabled = !state.busy,
                    onClick = viewModel::verifyCode,
                )
                OutlineActionButton(
                    text = stringResource(R.string.change_password_resend),
                    onClick = { if (!state.busy) viewModel.sendCode() },
                )
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }

            ChangePasswordStep.VERIFIED -> ChangePasswordColumn(innerPadding) {
                ScreenDescription(text = "Define tu nueva contraseña.")
                RuleValueField(
                    value = state.password,
                    onValueChange = viewModel::editPassword,
                    placeholder = stringResource(R.string.change_password_new_password),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                )
                RuleValueField(
                    value = state.confirmation,
                    onValueChange = viewModel::editConfirmation,
                    placeholder = stringResource(R.string.change_password_confirm_password),
                    keyboardType = KeyboardType.Password,
                    isPassword = true,
                )
                PrimaryButton(
                    text = stringResource(R.string.change_password_save),
                    enabled = !state.busy,
                    onClick = viewModel::save,
                )
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            }

            ChangePasswordStep.SAVED -> {
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    if (state.step == ChangePasswordStep.SAVED) {
        ValidationDialog(
            titleRes = R.string.change_password_success_title,
            message = stringResource(R.string.change_password_success_message),
            buttonTextRes = R.string.change_password_go_login,
            onCorrectClick = onSessionEnded,
        )
    }
}

@Composable
private fun ChangePasswordColumn(
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(8.dp))
        content()
        Spacer(Modifier.height(24.dp))
    }
}
