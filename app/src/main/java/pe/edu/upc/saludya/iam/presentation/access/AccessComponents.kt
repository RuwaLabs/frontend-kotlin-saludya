package pe.edu.upc.saludya.iam.presentation.access

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
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.iam.domain.repository.AccessFailure

@Composable
fun IamPage(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().imePadding()
        .verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.iam_brand), style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.headlineSmall)
        content()
    }
}

@Composable
fun IamButton(label: String, enabled: Boolean = true, outlined: Boolean = false, onClick: () -> Unit) {
    val modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
    if (outlined) OutlinedButton(onClick, modifier, enabled, shape = RoundedCornerShape(12.dp)) { Text(label) }
    else Button(onClick, modifier, enabled, shape = RoundedCornerShape(12.dp)) { Text(label) }
}

@Composable
fun IamMessage(message: String, error: Boolean = false) {
    Text(message, color = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
}

@Composable
fun LoginPassword(value: String, enabled: Boolean, onChange: (String) -> Unit,
                  label: Int = R.string.iam_password, error: String? = null) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(value, onChange, enabled = enabled, singleLine = true,
        label = { Text(stringResource(label)) }, isError = error != null,
        supportingText = if (error != null) ({ Text(error) }) else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = { TextButton({ visible = !visible }, enabled = enabled) {
            Text(stringResource(if (visible) R.string.iam_hide_password else R.string.iam_show_password))
        } }, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
}

@Composable
fun CodeVerificationScreen(
    recipient: String, code: String, busy: Boolean, message: String?, error: Boolean,
    onCode: (String) -> Unit, onVerify: () -> Unit, onResend: () -> Unit, onBack: () -> Unit,
    forLogin: Boolean = false
) {
    IamPage(stringResource(if (forLogin) R.string.iam_login_code_title else R.string.iam_registration_code_title)) {
        Text(stringResource(R.string.iam_code_instruction, recipient))
        OutlinedTextField(code, onCode, enabled = !busy, singleLine = true, isError = error,
            label = { Text(stringResource(R.string.iam_code_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        Text(stringResource(if (forLogin) R.string.iam_login_code_expiration else R.string.iam_code_expiration),
            style = MaterialTheme.typography.bodySmall)
        if (busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        if (message != null) IamMessage(message, error)
        IamButton(stringResource(if (forLogin) R.string.iam_sign_in else R.string.iam_create_account), !busy, onClick = onVerify)
        IamButton(stringResource(R.string.iam_resend_code), !busy, outlined = true, onClick = onResend)
        IamButton(stringResource(R.string.iam_back), !busy, outlined = true, onClick = onBack)
    }
}

fun AccessFailure.messageResource(): Int = when (this) {
    AccessFailure.RESET_LINK_INVALID -> R.string.iam_reset_link_invalid
    AccessFailure.PASSWORD_MISMATCH -> R.string.iam_password_mismatch
    AccessFailure.SESSION_EXPIRED -> R.string.iam_session_expired
    AccessFailure.INVALID_CREDENTIALS -> R.string.iam_invalid_credentials
    AccessFailure.INACTIVE_ACCOUNT -> R.string.iam_inactive_account
    AccessFailure.INVALID_CODE -> R.string.iam_invalid_code
    AccessFailure.EXPIRED_CODE -> R.string.iam_expired_code
    AccessFailure.TOO_MANY_ATTEMPTS -> R.string.iam_too_many_attempts
    AccessFailure.NOT_PATIENT -> R.string.iam_patient_only
    AccessFailure.INVALID_REQUEST -> R.string.iam_access_invalid_request
    AccessFailure.UNAVAILABLE -> R.string.iam_access_unavailable
    AccessFailure.UNEXPECTED -> R.string.iam_access_error
}
