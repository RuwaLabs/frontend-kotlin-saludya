package pe.edu.upc.saludya.checkin.presentation.queue

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.saludya.checkin.domain.entity.AttendanceQueueStatus
import pe.edu.upc.saludya.checkin.domain.entity.QueuePosition
import pe.edu.upc.saludya.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.checkin.presentation.components.InfoCard
import pe.edu.upc.saludya.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.checkin.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.checkin.presentation.components.toDoctorLabel
import pe.edu.upc.saludya.checkin.presentation.components.toHourLabel
import pe.edu.upc.saludya.checkin.presentation.components.toWaitingLocationLabel
import pe.edu.upc.saludya.checkin.presentation.components.CheckInPreviewData

/**
 * Pantalla "Mi turno" (posición en la cola) — punto de entrada con estado.
 *
 * @param appointmentId `appointments.id` de la cita con check-in.
 * @param onViewTicket abre el ticket de atención.
 * @param onTurnCalled el consultorio llamó al paciente; navegar a "¡Es tu turno!".
 * @param onLeftQueue el paciente dejó la cola; cerrar el flujo.
 */
@Composable
fun QueueScreen(
    appointmentId: Int,
    onBackClick: () -> Unit,
    onViewTicket: () -> Unit,
    onTurnCalled: () -> Unit,
    onLeftQueue: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QueueViewModel = viewModel(
        key = "queue-$appointmentId",
        factory = QueueViewModel.factory(appointmentId),
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isCalled) {
        if (uiState.isCalled) onTurnCalled()
    }
    LaunchedEffect(uiState.hasLeftQueue) {
        if (uiState.hasLeftQueue) onLeftQueue()
    }

    QueueContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewTicket = onViewTicket,
        onLeaveQueueClick = viewModel::onLeaveQueueClick,
        onLeaveQueueConfirm = viewModel::onLeaveQueueConfirm,
        onLeaveQueueDismiss = viewModel::onLeaveQueueDismiss,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    )
}

/** Contenido sin estado de la pantalla. */
@Composable
fun QueueContent(
    uiState: QueueUiState,
    onBackClick: () -> Unit,
    onViewTicket: () -> Unit,
    onLeaveQueueClick: () -> Unit,
    onLeaveQueueConfirm: () -> Unit,
    onLeaveQueueDismiss: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MaterialTheme.colorScheme

    CheckInScaffold(
        title = "Mi turno",
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        val position = uiState.position
        if (ticket == null || position == null) {
            InfoNote(text = uiState.errorMessage ?: "No tienes un turno activo en la cola de atención.")
            return@CheckInScaffold
        }
        val reservation = ticket.reservation

        if (uiState.isQueueVisible) {
            Text(
                text = "Tu posición actual",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${position.current} de ${position.total}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = peopleAheadLabel(position.peopleAhead),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurface,
            )
        } else {
            // hospital_configurations.attendance_queue_visible = FALSE
            Text(
                text = "Estás en la cola de atención. Te avisaremos cuando sea tu turno.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurface,
            )
        }

        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = reservation.specialty,
            lines = listOfNotNull(
                "Turno ${ticket.code} · ${reservation.patientFullName}",
                reservation.toWaitingLocationLabel(),
                reservation.toDoctorLabel(),
            ),
            highlight = "Llegada registrada · ${ticket.checkIn.checkedInAt.toHourLabel()}",
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = queueNote(position),
            style = MaterialTheme.typography.bodySmall,
            color = colors.onSurfaceVariant,
        )

        if (uiState.errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = colors.error,
            )
        }

        Spacer(Modifier.height(16.dp))

        OutlineActionButton(text = "Ver mi ticket", onClick = onViewTicket)

        Spacer(Modifier.height(12.dp))

        OutlineActionButton(text = "Dejar la cola", onClick = onLeaveQueueClick)
    }

    if (uiState.isLeaveDialogVisible) {
        AlertDialog(
            onDismissRequest = onLeaveQueueDismiss,
            title = { Text("¿Dejar la cola?") },
            text = { Text("Perderás tu lugar en la cola de atención. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = onLeaveQueueConfirm) { Text("Dejar la cola") }
            },
            dismissButton = {
                TextButton(onClick = onLeaveQueueDismiss) { Text("Cancelar") }
            },
        )
    }
}

private fun peopleAheadLabel(peopleAhead: Int): String = when (peopleAhead) {
    0 -> "Eres la siguiente persona en la cola."
    1 -> "Hay 1 persona delante de ti."
    else -> "Hay $peopleAhead personas delante de ti."
}

private fun queueNote(position: QueuePosition): String {
    val status = when (position.queueStatus) {
        AttendanceQueueStatus.OPEN -> "El orden sigue la llegada presencial."
        AttendanceQueueStatus.PAUSED -> "La cola está en pausa por el momento."
        AttendanceQueueStatus.CLOSED -> "La cola de atención está cerrada."
    }
    return "$status\nActualizado a las ${position.updatedAt.toHourLabel()}."
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QueueContentPreview() {
    MaterialTheme {
        QueueContent(
            uiState = QueueUiState(
                ticket = CheckInPreviewData.ticket,
                position = CheckInPreviewData.position,
            ),
            onBackClick = {},
            onViewTicket = {},
            onLeaveQueueClick = {},
            onLeaveQueueConfirm = {},
            onLeaveQueueDismiss = {},
            onBottomNavItemSelected = {},
        )
    }
}
