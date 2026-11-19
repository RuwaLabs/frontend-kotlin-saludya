package pe.edu.upc.saludya.features.checkin.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R

@Composable
fun AttendedDialog(onGoHome: () -> Unit) {
    AlertDialog(
        onDismissRequest = onGoHome,
        title = {
            Text(stringResource(R.string.checkin_attended_title))
        },
        text = {
            Text(stringResource(R.string.checkin_attended_message))
        },
        confirmButton = {
            TextButton(onClick = onGoHome) {
                Text(stringResource(R.string.checkin_go_home))
            }
        },
    )
}
