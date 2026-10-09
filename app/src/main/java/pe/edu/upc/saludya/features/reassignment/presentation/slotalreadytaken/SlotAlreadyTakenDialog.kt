package pe.edu.upc.saludya.features.reassignment.presentation.slotalreadytaken

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun SlotAlreadyTakenDialog(
    model: SlotAlreadyTakenUiModel = SlotAlreadyTakenUiModel(),
    onDismissRequest: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = model.title,
        description = model.description,
        primaryText = model.primaryText,
        onPrimaryClick = onViewAppointmentClick,
        onDismissRequest = onDismissRequest,
        secondaryText = model.secondaryText,
    )
}

@Preview(showBackground = true)
@Composable
private fun SlotAlreadyTakenDialogPreview() {
    SlotAlreadyTakenDialog()
}
