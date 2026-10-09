package pe.edu.upc.saludya.iam.presentation.access

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.iam.domain.model.VerifiedIdentity
import pe.edu.upc.saludya.iam.presentation.identity.IdentityVerificationRoute
import pe.edu.upc.saludya.iam.presentation.registration.RegistrationRoute

data class RecoveryLinkEvent(val id: Long, val url: String) {
    override fun toString(): String = "RecoveryLinkEvent[REDACTED]"
}

private enum class IamDestination { WELCOME, LOGIN, IDENTITY, REGISTRATION, RECOVERY, RESET_PASSWORD, HELP }

@Composable
fun IamRoot(
    recoveryLink: RecoveryLinkEvent? = null,
    loginViewModel: LoginViewModel = hiltViewModel(),
    recoveryViewModel: RecoveryViewModel = hiltViewModel(),
    resetViewModel: PasswordResetViewModel = hiltViewModel(),
    helpViewModel: RecoveryHelpViewModel = hiltViewModel()
) {
    val loginState by loginViewModel.state.collectAsState()
    var destination by rememberSaveable { mutableStateOf(if (loginState.session != null) IamDestination.LOGIN else IamDestination.WELCOME) }
    var identity by remember { mutableStateOf<VerifiedIdentity?>(null) }
    LaunchedEffect(recoveryLink?.id) {
        recoveryLink?.let { resetViewModel.acceptLink(it.url); destination = IamDestination.RESET_PASSWORD }
    }
    val recoveryDestination = destination in setOf(IamDestination.RESET_PASSWORD, IamDestination.RECOVERY, IamDestination.HELP)
    if (!recoveryDestination && loginState.notice == LoginNotice.SESSION_EXPIRED) {
        BackHandler { }
        SessionExpiredScreen { loginViewModel.acknowledgeExpiration(); destination = IamDestination.LOGIN }
        return
    }
    if (loginState.session != null && !recoveryDestination) {
        LaunchedEffect(loginState.session) { loginViewModel.observeExpiration() }
        BackHandler { }
        PatientSessionScreen(loginState, loginViewModel::requestLogout, loginViewModel::cancelLogout,
            loginViewModel::logout, loginViewModel::refreshProfile)
        return
    }
    when (destination) {
        IamDestination.WELCOME -> WelcomeScreen(
            onLogin = { loginViewModel.reset(); destination = IamDestination.LOGIN },
            onRegister = { identity = null; destination = IamDestination.IDENTITY })
        IamDestination.LOGIN -> {
            val back = { loginViewModel.reset(); destination = IamDestination.WELCOME }
            BackHandler {
                if (!loginState.busy) {
                    if (loginState.challenge != null) loginViewModel.cancelChallenge() else back()
                }
            }
            if (loginState.challenge != null) {
                CodeVerificationScreen(loginState.challenge!!.maskedEmail, loginState.code, loginState.busy,
                    loginMessage(loginState), loginState.failure != null || loginState.notice == LoginNotice.CODE_FORMAT,
                    loginViewModel::editCode, loginViewModel::verify, loginViewModel::resend,
                    loginViewModel::cancelChallenge, forLogin = true)
            } else LoginScreen(loginState, loginViewModel::editEmail, loginViewModel::editPassword,
                loginViewModel::login, onRecover = {
                    loginViewModel.reset(); recoveryViewModel.reset(); destination = IamDestination.RECOVERY
                }, onBack = back)
        }
        IamDestination.IDENTITY -> {
            BackHandler { destination = IamDestination.WELCOME }
            IdentityVerificationRoute(onContinueRegistration = { verified -> identity = verified; destination = IamDestination.REGISTRATION })
        }
        IamDestination.REGISTRATION -> {
            val verified = identity
            if (verified == null) {
                BackHandler { destination = IamDestination.WELCOME }
                IdentityVerificationRoute(onContinueRegistration = { identity = it })
            } else RegistrationRoute(verified, onBack = { identity = null; destination = IamDestination.IDENTITY },
                onLogin = { identity = null; loginViewModel.reset(); destination = IamDestination.LOGIN })
        }
        IamDestination.RECOVERY -> {
            val state by recoveryViewModel.state.collectAsState()
            val back = { recoveryViewModel.reset(); destination = IamDestination.LOGIN }
            BackHandler { if (!state.busy) back() }
            RecoveryScreen(state, recoveryViewModel::edit, recoveryViewModel::request, back,
                onReset = { resetViewModel.reset(); destination = IamDestination.RESET_PASSWORD },
                onHelp = { helpViewModel.reset(); destination = IamDestination.HELP })
        }
        IamDestination.RESET_PASSWORD -> {
            val state by resetViewModel.state.collectAsState()
            val back = { resetViewModel.reset(); loginViewModel.reset(); destination = IamDestination.LOGIN }
            BackHandler { if (!state.busy) back() }
            PasswordResetScreen(state, resetViewModel::editPassword, resetViewModel::editConfirmation,
                resetViewModel::save, resetViewModel::editLink, resetViewModel::openPastedLink,
                onNewLink = { resetViewModel.reset(); recoveryViewModel.reset(); destination = IamDestination.RECOVERY },
                onLogin = back)
        }
        IamDestination.HELP -> {
            val state by helpViewModel.state.collectAsState()
            val back = { helpViewModel.reset(); destination = IamDestination.LOGIN }
            BackHandler { if (!state.busy) back() }
            RecoveryHelpScreen(state, helpViewModel::editDni, helpViewModel::editEmail, helpViewModel::submit, back)
        }
    }
}

@Composable
fun WelcomeScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    IamPage(stringResource(R.string.iam_welcome_title)) {
        Text(stringResource(R.string.iam_welcome_description))
        IamButton(stringResource(R.string.iam_sign_in), onClick = onLogin)
        IamButton(stringResource(R.string.iam_register), outlined = true, onClick = onRegister)
    }
}

@Composable
fun LoginScreen(state: LoginUiState, onEmail: (String) -> Unit, onPassword: (String) -> Unit,
                onSubmit: () -> Unit, onRecover: () -> Unit, onBack: () -> Unit) {
    IamPage(stringResource(R.string.iam_sign_in)) {
        Text(stringResource(R.string.iam_login_description))
        OutlinedTextField(state.email, onEmail, enabled = !state.busy, singleLine = true,
            label = { Text(stringResource(R.string.iam_email)) },
            isError = state.notice == LoginNotice.EMAIL_FORMAT,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        LoginPassword(state.password, !state.busy, onPassword)
        if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        loginMessage(state)?.let { IamMessage(it, state.failure != null ||
            state.notice in setOf(LoginNotice.EMAIL_FORMAT, LoginNotice.PASSWORD_REQUIRED)) }
        IamButton(stringResource(R.string.iam_sign_in), !state.busy, onClick = onSubmit)
        IamButton(stringResource(R.string.iam_forgot_password), !state.busy, outlined = true, onClick = onRecover)
        IamButton(stringResource(R.string.iam_back), !state.busy, outlined = true, onClick = onBack)
    }
}

@Composable
private fun loginMessage(state: LoginUiState): String? {
    val resource = state.failure?.messageResource() ?: when (state.notice) {
        LoginNotice.EMAIL_FORMAT -> R.string.iam_email_error
        LoginNotice.PASSWORD_REQUIRED -> R.string.iam_password_required
        LoginNotice.CODE_FORMAT -> R.string.iam_code_format
        LoginNotice.CODE_SENT -> R.string.iam_code_sent
        LoginNotice.SESSION_EXPIRED -> R.string.iam_session_expired
        LoginNotice.LOGOUT_UNCONFIRMED -> R.string.iam_logout_unconfirmed
        null -> null
    }
    return resource?.let { stringResource(it) }
}

@Composable
fun PatientSessionScreen(state: LoginUiState, onLogout: () -> Unit,
    onCancelLogout: () -> Unit, onConfirmLogout: () -> Unit, onRefreshProfile: () -> Unit) {
    IamPage(stringResource(R.string.iam_session_title)) {
        Text(stringResource(R.string.iam_session_open))
        state.profile?.let { profile ->
            Text("${profile.name} ${profile.lastname}")
            Text(profile.email)
        }
        state.failure?.let { IamMessage(stringResource(it.messageResource()), error = true) }
        if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        IamButton(stringResource(R.string.iam_profile_refresh), !state.busy, outlined = true, onClick = onRefreshProfile)
        IamButton(stringResource(R.string.iam_logout), !state.busy, onClick = onLogout)
    }
    if (state.logoutConfirmation) AlertDialog(
        onDismissRequest = onCancelLogout,
        title = { Text(stringResource(R.string.iam_logout_confirm_title)) },
        text = { Text(stringResource(R.string.iam_logout_confirm_description)) },
        confirmButton = { TextButton(onConfirmLogout) { Text(stringResource(R.string.iam_logout)) } },
        dismissButton = { TextButton(onCancelLogout) { Text(stringResource(R.string.iam_cancel)) } })
}

@Composable
fun RecoveryScreen(state: RecoveryUiState, onEmail: (String) -> Unit, onRequest: () -> Unit,
                   onBack: () -> Unit, onReset: () -> Unit = {}, onHelp: () -> Unit = {}) {
    IamPage(stringResource(R.string.iam_recovery_title)) {
        Text(stringResource(R.string.iam_recovery_description))
        OutlinedTextField(state.email, onEmail, enabled = !state.busy, singleLine = true,
            label = { Text(stringResource(R.string.iam_email)) }, isError = state.invalidEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth())
        if (state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        if (state.invalidEmail) IamMessage(stringResource(R.string.iam_email_error), error = true)
        if (state.requested) {
            IamMessage(stringResource(R.string.iam_recovery_generic))
            Text(stringResource(R.string.iam_recovery_expiration), style = MaterialTheme.typography.bodySmall)
        }
        state.failure?.let { IamMessage(stringResource(it.messageResource()), error = true) }
        IamButton(stringResource(if (state.requested) R.string.iam_recovery_again else R.string.iam_recovery_send),
            !state.busy, onClick = onRequest)
        IamButton(stringResource(R.string.iam_reset_have_link), !state.busy, outlined = true, onClick = onReset)
        IamButton(stringResource(R.string.iam_help_no_access), !state.busy, outlined = true, onClick = onHelp)
        IamButton(stringResource(R.string.iam_recovery_back), !state.busy, outlined = true, onClick = onBack)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun LoginPreview() { SaludYaTheme(dynamicColor = false) { LoginScreen(LoginUiState(), {}, {}, {}, {}, {}) } }

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun VerificationPreview() {
    SaludYaTheme(dynamicColor = false) { CodeVerificationScreen("l***@example.com", "", false, null, false, {}, {}, {}, {}) }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun RecoveryPreview() { SaludYaTheme(dynamicColor = false) { RecoveryScreen(RecoveryUiState(requested = true), {}, {}, {}) } }
