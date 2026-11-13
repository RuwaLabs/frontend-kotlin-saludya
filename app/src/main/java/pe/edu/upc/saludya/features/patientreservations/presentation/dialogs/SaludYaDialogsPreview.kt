package pe.edu.upc.saludya.features.patientreservations.presentation.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

@Preview(showBackground = true, name = "R11 · Cupo ocupado")
@Composable
private fun CupoOcupadoDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        CupoOcupadoDialog(onViewSlots = {})
    }
}

@Preview(showBackground = true, name = "R12 · Cruce de reservas")
@Composable
private fun CruceReservasDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        CruceReservasDialog(beneficiaryName = "Lucía Torres", onChooseAnotherSlot = {})
    }
}

@Preview(showBackground = true, name = "R13 · Confirmar cancelación")
@Composable
private fun ConfirmarCancelacionDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        ConfirmarCancelacionDialog(
            summaryLines = listOf("Medicina General · 6 oct · 09:00", "Lucía Torres"),
            onCancelReservation = {},
            onKeepAppointment = {}
        )
    }
}

@Preview(showBackground = true, name = "R14 · Cancelación fuera de plazo")
@Composable
private fun CancelacionFueraPlazoDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        CancelacionFueraPlazoDialog(onBackToDetail = {})
    }
}

@Preview(showBackground = true, name = "R15 · Correo pendiente")
@Composable
private fun CorreoPendienteDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        CorreoPendienteDialog(onViewReservation = {})
    }
}

@Preview(showBackground = true, name = "R16 · Confirmar cita")
@Composable
private fun ConfirmarCitaDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        ConfirmarCitaDialog(
            summaryLines = listOf(
                "Lucía Torres · Titular",
                "Medicina General · Dra. Lucía Torres",
                "Mar 6 oct 2026 · 09:00–09:30",
                "Consultorio 02"
            ),
            onConfirm = {},
            onCancel = {}
        )
    }
}
