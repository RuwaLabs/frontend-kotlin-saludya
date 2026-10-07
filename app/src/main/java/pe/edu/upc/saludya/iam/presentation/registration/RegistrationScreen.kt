package pe.edu.upc.saludya.iam.presentation.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.RegistrationReceipt
import java.time.LocalDate

@Composable
fun RegistrationRoute(
    identity: VerifiedIdentity,
    onBack: () -> Unit,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    LaunchedEffect(identity) { viewModel.prepare(identity) }
    val state by viewModel.state.collectAsState()
    var emailStep by remember { mutableStateOf(false) }
    val receipt = state.receipt
    if (emailStep && receipt != null) {
        BackHandler { emailStep = false }
        RegistrationEmailStep(receipt.email, onBack = { emailStep = false })
    } else {
        // Avoid navigating away while a non-idempotent registration request is in flight.
        BackHandler(enabled = state.status == RegistrationStatus.SUBMITTING) { }
        RegistrationScreen(state, viewModel::edit, viewModel::submit, onBack,
            onContinueEmail = { if (state.canContinue) emailStep = true })
    }
}

@Composable
fun RegistrationScreen(
    state: RegistrationUiState,
    onEdit: (CredentialsField, String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onContinueEmail: () -> Unit
) {
    val loading = state.status == RegistrationStatus.SUBMITTING
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding()
        .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.iam_brand), style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.iam_credentials_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.iam_credentials_description), color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (state.receipt == null) {
            CredentialsInput(CredentialsField.EMAIL, state.form.email, R.string.iam_email,
                R.string.iam_email_hint, state, onEdit, keyboardType = KeyboardType.Email)
            CredentialsInput(CredentialsField.PASSWORD, state.form.password, R.string.iam_password,
                R.string.iam_password_hint, state, onEdit, password = true)
            CredentialsInput(CredentialsField.CONFIRM_PASSWORD, state.form.confirmPassword,
                R.string.iam_confirm_password, R.string.iam_confirm_password_hint, state, onEdit, password = true)
            CredentialsInput(CredentialsField.PHONE, state.form.phone, R.string.iam_phone,
                R.string.iam_phone_hint, state, onEdit, keyboardType = KeyboardType.Phone)
            Text(stringResource(R.string.iam_email_channel), style = MaterialTheme.typography.bodySmall)
        }
        if (loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text(stringResource(R.string.iam_submitting), modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        val message = when (state.status) {
            RegistrationStatus.ACCEPTED -> R.string.iam_credentials_accepted
            RegistrationStatus.IDENTITY_REQUIRED -> R.string.iam_identity_required
            RegistrationStatus.IDENTITY_ALREADY_REGISTERED -> R.string.iam_identity_already_registered
            RegistrationStatus.IDENTITY_MISMATCH -> R.string.iam_registration_identity_mismatch
            RegistrationStatus.ADULT_REQUIRED -> R.string.iam_adult_required
            RegistrationStatus.DUPLICATE_RECORD -> R.string.iam_duplicate_record
            RegistrationStatus.INVALID_REQUEST -> R.string.iam_registration_invalid_request
            RegistrationStatus.UNAVAILABLE -> R.string.iam_registration_unavailable
            RegistrationStatus.ERROR -> R.string.iam_registration_error
            else -> null
        }
        if (message != null) {
            Text(stringResource(message), color = if (state.canContinue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        if (state.receipt != null) {
            Text(state.receipt.email)
            Button(onClick = onContinueEmail, enabled = state.canContinue,
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(stringResource(R.string.iam_continue_email))
            }
        } else {
            Button(onClick = onSubmit, enabled = state.canSubmit,
                shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(stringResource(R.string.iam_credentials_continue))
            }
        }
        OutlinedButton(onClick = onBack, enabled = !loading,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.iam_back))
        }
    }
}

@Composable
private fun CredentialsInput(
    field: CredentialsField, value: String, label: Int, hint: Int,
    state: RegistrationUiState, onEdit: (CredentialsField, String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text, password: Boolean = false
) {
    var visible by remember { mutableStateOf(false) }
    val error = state.errors[field]
    OutlinedTextField(value = value, onValueChange = { onEdit(field, it) },
        enabled = state.canSubmit, singleLine = true, isError = error != null,
        label = { Text(stringResource(label)) }, placeholder = { Text(stringResource(hint)) },
        visualTransformation = if (password && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (password) KeyboardType.Password else keyboardType),
        trailingIcon = if (password) ({
            TextButton(onClick = { visible = !visible }, enabled = state.canSubmit) {
                Text(stringResource(if (visible) R.string.iam_hide_password else R.string.iam_show_password))
            }
        }) else null,
        supportingText = if (error != null) ({ Text(stringResource(when (error) {
            CredentialsError.EMAIL_FORMAT -> R.string.iam_email_error
            CredentialsError.EMAIL_IN_USE -> R.string.iam_email_in_use
            CredentialsError.PASSWORD_POLICY -> R.string.iam_password_error
            CredentialsError.PASSWORD_MISMATCH -> R.string.iam_password_mismatch
            CredentialsError.PHONE_FORMAT -> R.string.iam_phone_error
        })) }) else null,
        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
}

/** Email verification itself belongs to the next story; this step does not claim a code was sent. */
@Composable
fun RegistrationEmailStep(email: String, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.iam_email_step_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.iam_email_step_description))
        Text(email, color = MaterialTheme.colorScheme.primary)
        OutlinedButton(onClick = onBack, shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.iam_back))
        }
    }
}

private val previewIdentity = VerifiedIdentity(IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1)))

@Preview(showBackground = true, name = "Credenciales", widthDp = 375, heightDp = 812)
@Composable
private fun CredentialsPreview() {
    SaludYaTheme(dynamicColor = false) {
        RegistrationScreen(RegistrationUiState(identity = previewIdentity), { _, _ -> }, {}, {}, {})
    }
}

@Preview(showBackground = true, name = "Errores del formulario", widthDp = 375, heightDp = 812)
@Composable
private fun CredentialsErrorsPreview() {
    SaludYaTheme(dynamicColor = false) {
        RegistrationScreen(RegistrationUiState(identity = previewIdentity,
            errors = mapOf(CredentialsField.EMAIL to CredentialsError.EMAIL_IN_USE,
                CredentialsField.CONFIRM_PASSWORD to CredentialsError.PASSWORD_MISMATCH,
                CredentialsField.PHONE to CredentialsError.PHONE_FORMAT)), { _, _ -> }, {}, {}, {})
    }
}

@Preview(showBackground = true, name = "Credenciales recibidas", widthDp = 375, heightDp = 812)
@Composable
private fun CredentialsAcceptedPreview() {
    SaludYaTheme(dynamicColor = false) {
        RegistrationScreen(RegistrationUiState(identity = previewIdentity, status = RegistrationStatus.ACCEPTED,
            receipt = RegistrationReceipt(1, 2, "lucia@example.com")), { _, _ -> }, {}, {}, {})
    }
}
