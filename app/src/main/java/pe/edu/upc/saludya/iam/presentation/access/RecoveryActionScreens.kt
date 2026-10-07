package pe.edu.upc.saludya.iam.presentation.access

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

@Composable
fun PasswordResetScreen(state: PasswordResetUiState, onPassword: (String) -> Unit,
    onConfirmation: (String) -> Unit, onSave: () -> Unit, onLink: (String) -> Unit,
    onOpenLink: () -> Unit, onNewLink: () -> Unit, onLogin: () -> Unit) {
    IamPage(stringResource(R.string.iam_reset_title)) {
        when {
            state.updated -> {
                IamMessage(stringResource(R.string.iam_reset_success))
                IamButton(stringResource(R.string.iam_sign_in), onClick = onLogin)
            }
            !state.validLink -> {
                state.failure?.let { IamMessage(stringResource(it.messageResource()), error = true) }
                Text(stringResource(R.string.iam_reset_paste))
                OutlinedTextField(state.linkInput, onLink, enabled = !state.busy, singleLine = true,
                    label = { Text(stringResource(R.string.iam_reset_link_label)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
                IamButton(stringResource(R.string.iam_reset_open), !state.busy, onClick = onOpenLink)
                IamButton(stringResource(R.string.iam_reset_request_new), !state.busy, outlined = true, onClick = onNewLink)
                IamButton(stringResource(R.string.iam_recovery_back), !state.busy, outlined = true, onClick = onLogin)
            }
            else -> {
                Text(stringResource(R.string.iam_reset_description))
                LoginPassword(state.password, !state.busy, onPassword, R.string.iam_new_password,
                    if (state.passwordInvalid) stringResource(R.string.iam_password_error) else null)
                LoginPassword(state.confirmation, !state.busy, onConfirmation, R.string.iam_confirm_password,
                    if (state.mismatch) stringResource(R.string.iam_password_mismatch) else null)
                state.failure?.takeIf { it != pe.edu.upc.saludya.iam.domain.repository.AccessFailure.PASSWORD_MISMATCH }
                    ?.let { IamMessage(stringResource(it.messageResource()), error = true) }
                if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
                IamButton(stringResource(R.string.iam_reset_save), !state.busy, onClick = onSave)
                IamButton(stringResource(R.string.iam_recovery_back), !state.busy, outlined = true, onClick = onLogin)
            }
        }
    }
}

@Composable
fun RecoveryHelpScreen(state: RecoveryHelpUiState, onDni: (String) -> Unit,
                       onEmail: (String) -> Unit, onSubmit: () -> Unit, onBack: () -> Unit) {
    IamPage(stringResource(R.string.iam_help_title)) {
        val receipt = state.receipt
        if (receipt == null) {
            Text(stringResource(R.string.iam_help_description))
            OutlinedTextField(state.dni, onDni, enabled = !state.busy, singleLine = true, isError = state.invalidDni,
                label = { Text(stringResource(R.string.iam_dni)) },
                supportingText = if (state.invalidDni) ({ Text(stringResource(R.string.iam_dni_error)) }) else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(state.email, onEmail, enabled = !state.busy, singleLine = true, isError = state.invalidEmail,
                label = { Text(stringResource(R.string.iam_help_contact_email)) },
                supportingText = if (state.invalidEmail) ({ Text(stringResource(R.string.iam_email_error)) }) else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
            if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
            state.failure?.let { IamMessage(stringResource(it.messageResource()), error = true) }
            IamButton(stringResource(R.string.iam_help_send), !state.busy, onClick = onSubmit)
        } else {
            IamMessage(stringResource(R.string.iam_help_success))
            Text(receipt.instructions?.takeIf { it.isNotBlank() } ?: stringResource(R.string.iam_help_identity_check))
            receipt.phone?.takeIf { it.isNotBlank() }?.let { Text(stringResource(R.string.iam_help_phone, it)) }
        }
        IamButton(stringResource(R.string.iam_recovery_back), !state.busy, outlined = true, onClick = onBack)
    }
}

@Composable
fun SessionExpiredScreen(onLogin: () -> Unit) {
    IamPage(stringResource(R.string.iam_expiration_title)) {
        IamMessage(stringResource(R.string.iam_expiration_description))
        IamButton(stringResource(R.string.iam_sign_in), onClick = onLogin)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun PasswordResetPreview() { SaludYaTheme(dynamicColor = false) {
    PasswordResetScreen(PasswordResetUiState(validLink = true), {}, {}, {}, {}, {}, {}, {})
} }

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun RecoveryHelpPreview() { SaludYaTheme(dynamicColor = false) {
    RecoveryHelpScreen(RecoveryHelpUiState(), {}, {}, {}, {})
} }
