package pe.edu.upc.saludya.features.notifications.presentation.reassignmentsuccess

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun ReassignmentSuccessDialog(
    model: ReassignmentSuccessUiModel = ReassignmentSuccessUiModel(),
    onDismissRequest: () -> Unit = {},
    onViewDetailClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = model.title,
        description = model.description,
        primaryText = model.primaryText,
        onPrimaryClick = onViewDetailClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
    )
}

@Preview(showBackground = true)
@Composable
private fun ReassignmentSuccessDialogPreview() {
    ReassignmentSuccessDialog()
}
