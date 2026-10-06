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
private val CardTextSecondary = Color(0xFF6E7978)
private val CardOutline = Color(0xFFE5E9E7)

@Composable
fun PatientInfoCard(
    fullName: String,
    role: String,
    documentType: String,
    documentNumberMasked: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardOutline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            Text(
                text = fullName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CardTextPrimary,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$role · $documentType $documentNumberMasked",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = CardTextSecondary,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "PatientInfoCard")
@Composable
private fun PatientInfoCardPreview() {
    SaludYaTheme(dynamicColor = false) {
        PatientInfoCard(
            fullName = "Lucía Torres",
            role = "Titular",
            documentType = "DNI",
            documentNumberMasked = "••••4821",
        )
    }
}