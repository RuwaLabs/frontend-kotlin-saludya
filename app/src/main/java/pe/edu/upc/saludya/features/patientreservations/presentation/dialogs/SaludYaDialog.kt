package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaOutlinedButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText

@Composable
internal fun SaludYaDialog(
    title: String,
    body: List<String>,
    primaryText: String,
    onPrimary: () -> Unit,
    onDismiss: () -> Unit,
    secondaryText: String? = null,
    onSecondary: (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = SaludYaColors.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = title, style = saludYaText(22, FontWeight.SemiBold))
                Text(
                    text = body.joinToString("\n"),
                    style = saludYaText(14, color = SaludYaColors.Muted)
                )
                SaludYaPrimaryButton(text = primaryText, onClick = onPrimary)
                if (secondaryText != null && onSecondary != null) {
                    SaludYaOutlinedButton(text = secondaryText, onClick = onSecondary)
                }
            }
        }
    }
}
