package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

@Preview(showBackground = true, name = "Componentes de reserva")
@Composable
private fun SaludYaComponentsPreview() {
    SaludYaTheme(dynamicColor = false) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SaludYaSearchField(
                label = "Buscar especialidad",
                value = "",
                onValueChange = {},
                placeholder = "Buscar por nombre…"
            )
            SaludYaInfoCard(
                title = "Medicina General",
                body = listOf("Dra. Lucía Torres", "Mar 6 oct 2026 · 09:00–09:30"),
                status = "Pendiente de confirmar"
            )
            SaludYaReservaSummaryCard(
                summary = ReservaSummaryUi(
                    specialityName = "Medicina General",
                    beneficiaryName = "Lucía Torres",
                    beneficiaryTitular = true,
                    doctorName = "Lucía",
                    doctorLastname = "Torres",
                    date = "2026-10-06",
                    startHour = "09:00",
                    endHour = "09:30",
                    room = "02"
                ),
                status = "Confirmada"
            )
            SaludYaPrimaryButton(text = "Continuar", onClick = {})
            SaludYaOutlinedButton(text = "Volver y corregir", onClick = {})
            SaludYaStateMessage(
                title = "Este día está completo",
                description = "Puedes elegir otra fecha con disponibilidad."
            )
        }
    }
}
