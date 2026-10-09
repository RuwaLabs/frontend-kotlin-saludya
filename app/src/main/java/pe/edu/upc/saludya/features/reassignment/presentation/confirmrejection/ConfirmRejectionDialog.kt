package pe.edu.upc.saludya.features.reassignment.presentation.confirmrejection

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun ConfirmRejectionDialog(
    model: ConfirmRejectionUiModel = ConfirmRejectionUiModel(),
    onDismissRequest: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = stringResource(R.string.confirm_rejection_title),
        description = listOf(stringResource(R.string.confirm_rejection_description)),
        primaryText = stringResource(R.string.confirm_rejection_primary),
        onPrimaryClick = onRejectClick,
        onDismissRequest = onDismissRequest,
        secondaryText = stringResource(R.string.confirm_rejection_secondary),
        onSecondaryClick = onBackClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun ConfirmRejectionDialogPreview() {
    ConfirmRejectionDialog()
}
