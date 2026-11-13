package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

data class InfoCardUiModel(
    val title: String,
    val lines: List<String> = emptyList(),
    val footer: String? = null,
)

@Composable
fun SaludYaInfoCard(
    model: InfoCardUiModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SaludYaColors.Surface)
            .border(1.dp, SaludYaColors.Border, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = model.title, style = SaludYaTextStyles.CardTitle)
        if (model.lines.isNotEmpty()) {
            Column {
                model.lines.forEach { line ->
                    Text(text = line, style = SaludYaTextStyles.CardBody)
                }
            }
        }
        model.footer?.let { footer ->
            Text(text = footer, style = SaludYaTextStyles.CardFooter)
        }
    }
}
