package pe.edu.upc.saludya.features.configuration.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

private val CardTextPrimary = Color(0xFF181C1C)
private val CardTextSecondary = Color(0xFF56706D)
private val CardOutline = Color(0xFFE5E9E7)

@Composable
fun AttentionSummaryCard(
    title: String,
    detailsLine: String,
    updatedLine: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 18.dp),
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = CardTextPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = detailsLine,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = CardTextSecondary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = updatedLine,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "AttentionSummaryCard")
@Composable
private fun AttentionSummaryCardPreview() {
    SaludYaTheme(dynamicColor = false) {
        AttentionSummaryCard(
            title = "Resumen de atención",
            detailsLine = "36 citas atendidas · 6 cupos recuperados",
            updatedLine = "Actualizado a las 16:30",
        )
    }
}