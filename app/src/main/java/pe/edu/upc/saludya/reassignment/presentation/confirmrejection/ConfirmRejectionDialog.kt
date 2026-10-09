package pe.edu.upc.saludya.reassignment.presentation.confirmrejection

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun ConfirmRejectionDialog(
    model: ConfirmRejectionUiModel = ConfirmRejectionUiModel(),
    onDismissRequest: () -> Unit = {},
    onRejectClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = model.title,
        description = model.description,
        primaryText = model.primaryText,
        onPrimaryClick = onRejectClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
        onSecondaryClick = onBackClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun ConfirmRejectionDialogPreview() {
    ConfirmRejectionDialog()
}
