package pe.edu.upc.saludya.features.checkin.presentation.queue

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
import pe.edu.upc.saludya.R
import androidx.compose.ui.res.stringResource
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceQueueStatus
import pe.edu.upc.saludya.features.checkin.domain.model.QueuePosition
import pe.edu.upc.saludya.features.checkin.presentation.components.AttendedDialog
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoCard
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.features.checkin.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.checkin.presentation.components.toDoctorLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toHourLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toWaitingLocationLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInPreviewData

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
    onAttended: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QueueViewModel = hiltViewModel(),
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
        onRefresh = viewModel::refresh,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    )

    if (uiState.isAttended) {
        AttendedDialog(onGoHome = onAttended)
    }
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
    onRefresh: () -> Unit = {},
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    CheckInScaffold(
        title = stringResource(R.string.checkin_queue_title),
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        val position = uiState.position
        if (ticket == null || position == null) {
            InfoNote(
                text = uiState.errorMessage
                    ?: stringResource(R.string.checkin_queue_no_active),
            )
            return@CheckInScaffold
        }
        val reservation = ticket.reservation

        if (uiState.isQueueVisible) {
            Text(
                text = stringResource(R.string.checkin_queue_current_position),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(
                    R.string.checkin_queue_position,
                    position.current,
                    position.total,
                ),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = peopleAheadLabel(context, position.peopleAhead),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurface,
            )
        } else {
            // hospital_configurations.attendance_queue_visible = FALSE
            Text(
                text = stringResource(R.string.checkin_queue_waiting_info),
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurface,
            )
        }

        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = reservation.specialty,
            lines = listOfNotNull(
                stringResource(
                    R.string.checkin_queue_ticket_patient,
                    ticket.code,
                    reservation.patientFullName,
                ),
                reservation.toWaitingLocationLabel(context),
                reservation.toDoctorLabel(context),
            ),
            highlight = stringResource(
                R.string.checkin_arrival_registered,
                ticket.checkIn.checkedInAt.toHourLabel(),
            ),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = queueNote(context, position),
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

        OutlineActionButton(
            text = stringResource(R.string.checkin_view_ticket),
            onClick = onViewTicket,
        )

        Spacer(Modifier.height(12.dp))
        OutlineActionButton(
            text = stringResource(R.string.checkin_queue_refresh_position),
            onClick = onRefresh,
        )

        Spacer(Modifier.height(12.dp))

        OutlineActionButton(
            text = stringResource(R.string.checkin_queue_leave_confirm),
            onClick = onLeaveQueueClick,
        )

        Spacer(Modifier.height(12.dp))

        InfoNote(
            text = stringResource(R.string.checkin_queue_leave_note),
        )
    }

    if (uiState.isLeaveDialogVisible) {
        AlertDialog(
            onDismissRequest = onLeaveQueueDismiss,
            title = {
                Text(stringResource(R.string.checkin_queue_leave_title))
            },
            text = {
                Text(stringResource(R.string.checkin_queue_leave_message))
            },
            confirmButton = {
                TextButton(onClick = onLeaveQueueConfirm) {
                    Text(stringResource(R.string.checkin_queue_leave_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onLeaveQueueDismiss) {
                    Text(stringResource(R.string.checkin_queue_leave_cancel))
                }
            },
        )
    }
}

private fun peopleAheadLabel(
    context: Context,
    peopleAhead: Int,
): String = when (peopleAhead) {
    0 -> context.getString(R.string.checkin_queue_next_person)
    1 -> context.getString(R.string.checkin_queue_one_person_ahead)
    else -> context.getString(
        R.string.checkin_queue_people_ahead,
        peopleAhead,
    )
}

private fun queueNote(
    context: Context,
    position: QueuePosition,
): String {
    val status = when (position.queueStatus) {
        AttendanceQueueStatus.OPEN ->
            context.getString(R.string.checkin_queue_status_open)

        AttendanceQueueStatus.PAUSED ->
            context.getString(R.string.checkin_queue_status_paused)

        AttendanceQueueStatus.CLOSED ->
            context.getString(R.string.checkin_queue_status_closed)

        null ->
            context.getString(R.string.checkin_queue_status_unknown)
    }

    return context.getString(
        R.string.checkin_queue_updated_at,
        status,
        position.updatedAt.toHourLabel(),
    )
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
