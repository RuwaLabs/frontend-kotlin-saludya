package pe.edu.upc.saludya.features.checkin.presentation.called

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoCard
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.features.checkin.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.checkin.presentation.components.toDoctorLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toHourLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInPreviewData

/**
 * Pantalla "¡Es tu turno!" (llamado al consultorio) — punto de entrada con estado.
 *
 * @param appointmentId `appointments.id` de la cita llamada.
 * @param onViewTicket abre el ticket de atención.
 */
@Composable
fun TurnCalledScreen(
    appointmentId: Int,
    onBackClick: () -> Unit,
    onViewTicket: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TurnCalledViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    TurnCalledContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewTicket = onViewTicket,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    )
}

/** Contenido sin estado de la pantalla. */
@Composable
fun TurnCalledContent(
    uiState: TurnCalledUiState,
    onBackClick: () -> Unit,
    onViewTicket: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CheckInScaffold(
        title = "¡Es tu turno!",
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        if (ticket == null) {
            InfoNote(text = uiState.errorMessage ?: "No se encontró tu turno.")
            return@CheckInScaffold
        }
        val reservation = ticket.reservation

        Spacer(Modifier.height(8.dp))

        Text(
            text = reservation.consultingRoom
                ?.let { "Dirígete al consultorio $it" }
                ?: "Dirígete a tu consultorio",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = "Turno ${ticket.code}",
            lines = listOfNotNull(
                "${reservation.patientFullName} · ${reservation.specialty}",
                reservation.toDoctorLabel(),
                ticket.queueEntry.calledAt?.let { "Llamada registrada: ${it.toHourLabel()}" },
            ),
        )

        Spacer(Modifier.height(16.dp))

        InfoNote(
            text = "Acércate dentro del plazo indicado por el establecimiento: " +
                toleranceLabel(uiState.postCallToleranceMinutes) + ".",
        )

        Spacer(Modifier.height(16.dp))

        OutlineActionButton(text = "Ver mi ticket", onClick = onViewTicket)
    }
}

private fun toleranceLabel(minutes: Int): String =
    if (minutes == 1) "1 minuto" else "$minutes minutos"

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TurnCalledContentPreview() {
    MaterialTheme {
        TurnCalledContent(
            uiState = TurnCalledUiState(ticket = CheckInPreviewData.calledTicket),
            onBackClick = {},
            onViewTicket = {},
            onBottomNavItemSelected = {},
        )
    }
}
