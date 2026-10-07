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
private val CardTextSecondary = Color(0xFF6E7978)
private val CardOutline = Color(0xFFE5E9E7)

@Composable
fun AppointmentCard(
    specialty: String,
    patientName: String,
    dateTime: String,
    doctorName: String,
    status: String,
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
                text = specialty,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CardTextPrimary,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = patientName,
                fontSize = 15.sp,
                color = CardTextSecondary,
            )
            Text(
                text = dateTime,
                fontSize = 15.sp,
                color = CardTextSecondary,
            )
            Text(
                text = doctorName,
                fontSize = 15.sp,
                color = CardTextSecondary,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = status,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "AppointmentCard")
@Composable
private fun AppointmentCardPreview() {
    SaludYaTheme(dynamicColor = false) {
        AppointmentCard(
            specialty = "Pediatría",
            patientName = "Mateo Torres",
            dateTime = "Mié 7 oct · 10:00–10:30",
            doctorName = "Dra. Carla Díaz",
            status = "Confirmada",
        )
    }
}