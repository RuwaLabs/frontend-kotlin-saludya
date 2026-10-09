package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val ButtonShape = RoundedCornerShape(12.dp)
private val ButtonPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)

@Composable
fun SaludYaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = ButtonShape,
        contentPadding = ButtonPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = SaludYaColors.Primary,
            contentColor = SaludYaColors.White,
            disabledContainerColor = SaludYaColors.Primary.copy(alpha = 0.5f),
            disabledContentColor = SaludYaColors.White
        )
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = saludYaText(15, FontWeight.SemiBold, SaludYaColors.White, letterSpacing = -0.15f)
        )
    }
}

@Composable
fun SaludYaOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = ButtonShape,
        contentPadding = ButtonPadding,
        border = BorderStroke(1.dp, SaludYaColors.Primary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SaludYaColors.White,
            contentColor = SaludYaColors.Primary
        )
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = saludYaText(15, FontWeight.SemiBold, SaludYaColors.Primary, letterSpacing = -0.15f)
        )
    }
}
