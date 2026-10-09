package pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.icon.health_cross
import pe.edu.upc.saludya.features.patientreservations.domain.model.Speciality
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText

@Composable
fun SpecialityCard(
    speciality: Speciality,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SaludYaColors.White),
        border = BorderStroke(1.dp, SaludYaColors.Border),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = health_cross,
                contentDescription = speciality.name,
                tint = SaludYaColors.Primary,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = speciality.name,
                style = saludYaText(13, FontWeight.SemiBold)
            )
            Text(
                text = stringResource(R.string.reservation_speciality_availability),
                style = saludYaText(10, color = SaludYaColors.Muted)
            )
        }
    }
}
