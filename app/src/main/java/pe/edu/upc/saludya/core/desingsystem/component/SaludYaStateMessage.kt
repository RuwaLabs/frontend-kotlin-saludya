package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaludYaStateMessage(
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Icon(
            imageVector = SaludYaIcons.Cross,
            contentDescription = null,
            tint = SaludYaColors.Primary,
            modifier = Modifier.size(48.dp),
        )
        Text(text = title, style = SaludYaTextStyles.StateTitle)
        Text(text = description, style = SaludYaTextStyles.StateBody)
        SaludYaPrimaryButton(text = buttonText, onClick = onButtonClick)
    }
}
