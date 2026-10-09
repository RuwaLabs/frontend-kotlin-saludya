package pe.edu.upc.saludya.features.reassignment.presentation.reassignmentmodal

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun ReassignmentModalDialog(
    model: ReassignmentModalUiModel = ReassignmentModalUiModel(),
    onDismissRequest: () -> Unit = {},
    onAcceptClick: () -> Unit = {},
    onRejectClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = stringResource(R.string.reassignment_modal_title),
        description = model.description,
        primaryText = stringResource(R.string.reassignment_modal_accept),
        onPrimaryClick = onAcceptClick,
        onDismissRequest = onDismissRequest,
        secondaryText = stringResource(R.string.reassignment_modal_reject),
        onSecondaryClick = onRejectClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun ReassignmentModalDialogPreview() {
    ReassignmentModalDialog()
}
