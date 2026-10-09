package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SaludYaDialog(
    title: String,
    description: List<String>,
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    onSecondaryClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = 24.dp)
                .widthIn(max = 327.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SaludYaColors.Surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(text = title, style = SaludYaTextStyles.DialogTitle)
            Column {
                description.forEach { line ->
                    Text(text = line, style = SaludYaTextStyles.DialogBody)
                }
            }
            SaludYaPrimaryButton(text = primaryText, onClick = onPrimaryClick)
            if (secondaryText != null) {
                SaludYaOutlinedButton(text = secondaryText, onClick = onSecondaryClick)
            }
        }
    }
}
