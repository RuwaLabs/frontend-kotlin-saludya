package pe.edu.upc.saludya.features.reassignment.presentation.slotalreadytaken

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDialog

@Composable
fun SlotAlreadyTakenDialog(
    model: SlotAlreadyTakenUiModel = SlotAlreadyTakenUiModel(),
    onDismissRequest: () -> Unit = {},
    onViewAppointmentClick: () -> Unit = {},
) {
    SaludYaDialog(
        title = stringResource(R.string.slot_taken_title),
        description = listOf(stringResource(R.string.slot_taken_description)),
        primaryText = stringResource(R.string.slot_taken_primary),
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
