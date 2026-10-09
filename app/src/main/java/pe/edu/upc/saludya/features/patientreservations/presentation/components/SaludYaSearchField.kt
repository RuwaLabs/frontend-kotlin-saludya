package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SaludYaSearchField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = label, style = saludYaText(12, FontWeight.SemiBold, SaludYaColors.Muted))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = saludYaText(14),
            cursorBrush = SolidColor(SaludYaColors.Primary),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SaludYaColors.White, RoundedCornerShape(10.dp))
                        .border(BorderStroke(1.dp, SaludYaColors.Border), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = saludYaText(14))
                    }
                    innerTextField()
                }
            }
        )
    }
}
