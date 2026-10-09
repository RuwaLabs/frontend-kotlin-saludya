package pe.edu.upc.saludya.reassignment.presentation.reassignmentmodal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun ReassignmentModalDialog(
    model: ReassignmentModalUiModel = ReassignmentModalUiModel(),
    onDismissRequest: () -> Unit = {},
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = model.title,
        description = model.description,
        primaryText = model.primaryText,
        onPrimaryClick = onAcceptClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
        onSecondaryClick = onRejectClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun ReassignmentModalDialogPreview() {
    ReassignmentModalDialog()
}
