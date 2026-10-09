package pe.edu.upc.saludya.checkin.presentation.ticket

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
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.saludya.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.checkin.presentation.components.InfoCard
import pe.edu.upc.saludya.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.checkin.presentation.components.PrimaryButton
import pe.edu.upc.saludya.checkin.presentation.components.toDoctorLabel
import pe.edu.upc.saludya.checkin.presentation.components.toHourLabel
import pe.edu.upc.saludya.checkin.presentation.components.toLocationLabel
import pe.edu.upc.saludya.checkin.presentation.components.toScheduleLabel
import pe.edu.upc.saludya.checkin.presentation.components.CheckInPreviewData

/**
 * Pantalla "Ticket de atención" — punto de entrada con estado.
 *
 * @param appointmentId `appointments.id` de la cita con check-in.
 * @param onViewPosition abre "Mi turno".
 */
@Composable
fun TicketScreen(
    appointmentId: Int,
    onBackClick: () -> Unit,
    onViewPosition: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketViewModel = viewModel(
        key = "ticket-$appointmentId",
        factory = TicketViewModel.factory(appointmentId),
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    TicketContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewPosition = onViewPosition,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    )
}

/** Contenido sin estado de la pantalla. */
@Composable
fun TicketContent(
    uiState: TicketUiState,
    onBackClick: () -> Unit,
    onViewPosition: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CheckInScaffold(
        title = "Ticket de atención",
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        if (ticket == null) {
            InfoNote(text = uiState.errorMessage ?: "Esta reserva aún no tiene una llegada registrada.")
            return@CheckInScaffold
        }
        val reservation = ticket.reservation

        Spacer(Modifier.height(8.dp))

        Text(
            text = ticket.code,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = reservation.patientFullName,
            lines = listOfNotNull(
                reservation.specialty,
                reservation.toDoctorLabel(),
                reservation.toScheduleLabel(),
                reservation.toLocationLabel(),
            ),
            highlight = "Llegada registrada · ${ticket.checkIn.checkedInAt.toHourLabel()}",
        )

        Spacer(Modifier.height(16.dp))

        InfoNote(text = "Conserva este ticket hasta terminar tu atención.")

        Spacer(Modifier.height(16.dp))

        PrimaryButton(text = "Ver mi posición", onClick = onViewPosition)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TicketContentPreview() {
    MaterialTheme {
        TicketContent(
            uiState = TicketUiState(ticket = CheckInPreviewData.ticket),
            onBackClick = {},
            onViewPosition = {},
            onBottomNavItemSelected = {},
        )
    }
}
