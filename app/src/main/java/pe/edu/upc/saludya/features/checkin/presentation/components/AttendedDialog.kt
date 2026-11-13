package pe.edu.upc.saludya.features.checkin.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AttendedDialog(onGoHome: () -> Unit) {
    AlertDialog(
        onDismissRequest = onGoHome,
        title = { Text("Ya has sido atendido") },
        text = { Text("Tu atención en el consultorio terminó. Puedes volver al inicio.") },
        confirmButton = {
            TextButton(onClick = onGoHome) { Text("Volver al inicio") }
        },
    )
}
