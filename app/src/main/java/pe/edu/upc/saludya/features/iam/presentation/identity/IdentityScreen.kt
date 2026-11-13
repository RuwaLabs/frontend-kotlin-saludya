package pe.edu.upc.saludya.features.iam.presentation.identity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.iam.domain.model.*

@Composable
fun IdentityVerificationRoute(
    onContinueRegistration: (VerifiedIdentity) -> Unit,
    viewModel: IdentityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    IdentityScreen(state, viewModel::edit, viewModel::verify) {
        if (state.canContinue) state.verifiedIdentity?.let(onContinueRegistration)
    }
}

@Composable
fun IdentityScreen(
    state: IdentityUiState,
    onEdit: (IdentityField, String) -> Unit,
    onVerify: () -> Unit,
    onContinue: () -> Unit
) {
    val loading = state.status == IdentityStatus.VERIFYING
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().imePadding()
            .verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.iam_brand), color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.iam_identity_title), style = MaterialTheme.typography.headlineSmall)
        Text(stringResource(R.string.iam_identity_description), color = MaterialTheme.colorScheme.onSurfaceVariant)
        IdentityInput(IdentityField.DNI, state.form.dni, R.string.iam_dni, R.string.iam_dni_hint, state, loading, onEdit, KeyboardType.Number)
        IdentityInput(IdentityField.NAMES, state.form.names, R.string.iam_names, R.string.iam_names_hint, state, loading, onEdit)
        IdentityInput(IdentityField.LAST_NAMES, state.form.lastNames, R.string.iam_last_names, R.string.iam_last_names_hint, state, loading, onEdit)
        IdentityInput(IdentityField.BIRTH_DATE, state.form.birthDate, R.string.iam_birth_date, R.string.iam_date_hint, state, loading, onEdit)
        if (loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Text(stringResource(R.string.iam_verifying), modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        val message = when (state.status) {
            IdentityStatus.VERIFIED -> R.string.iam_verified
            IdentityStatus.MISMATCH -> R.string.iam_mismatch
            IdentityStatus.UNAVAILABLE -> R.string.iam_unavailable
            IdentityStatus.INVALID_REQUEST -> R.string.iam_invalid_request
            IdentityStatus.ERROR -> R.string.iam_unexpected_error
            else -> null
        }
        if (message != null) {
            Text(stringResource(message),
                color = if (state.canContinue) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
        }
        Button(onClick = onVerify, enabled = !loading && !state.canContinue,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(if (state.status == IdentityStatus.UNAVAILABLE || state.status == IdentityStatus.ERROR) R.string.iam_retry else R.string.iam_verify_identity))
        }
        OutlinedButton(onClick = onContinue, enabled = state.canContinue,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.iam_continue_registration))
        }
    }
}

@Composable
private fun IdentityInput(
    field: IdentityField, value: String, label: Int, hint: Int,
    state: IdentityUiState, loading: Boolean, onEdit: (IdentityField, String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val error = state.errors[field]
    OutlinedTextField(value = value, onValueChange = { onEdit(field, it) },
        label = { Text(stringResource(label)) }, placeholder = { Text(stringResource(hint)) },
        enabled = !loading, singleLine = true, isError = error != null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(),
        supportingText = if (error != null) ({ Text(stringResource(when (error) {
            IdentityValidationError.DNI_FORMAT -> R.string.iam_dni_error
            IdentityValidationError.REQUIRED -> R.string.iam_required
            IdentityValidationError.NAME_TOO_LONG -> R.string.iam_name_too_long
            IdentityValidationError.DATE_FORMAT -> R.string.iam_date_error
            IdentityValidationError.DATE_NOT_PAST -> R.string.iam_date_not_past
        })) }) else null)
}

/** Handoff target for US-01. Account creation is implemented by the registration story. */
@Composable
fun VerifiedIdentitySummary(identity: VerifiedIdentity, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.iam_verified), style = MaterialTheme.typography.headlineSmall)
        Text("${identity.details.names} ${identity.details.lastNames}")
        Text(stringResource(R.string.iam_ready_to_register))
        OutlinedButton(onClick = onBack) { Text(stringResource(R.string.iam_back)) }
    }
}
