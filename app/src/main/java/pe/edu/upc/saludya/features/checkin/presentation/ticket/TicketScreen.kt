package pe.edu.upc.saludya.features.checkin.presentation.ticket

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
import pe.edu.upc.saludya.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.features.checkin.presentation.components.AttendedDialog
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoCard
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.features.checkin.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.checkin.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.checkin.presentation.components.toDoctorLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toHourLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toLocationLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.toScheduleLabel
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInPreviewData

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
    onAttended: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TicketViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    TicketContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewPosition = onViewPosition,
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
fun TicketContent(
    uiState: TicketUiState,
    onBackClick: () -> Unit,
    onViewPosition: () -> Unit,
    onRefresh: () -> Unit = {},
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    CheckInScaffold(
        title = stringResource(R.string.checkin_ticket_title),
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        if (ticket == null) {
            InfoNote(
                text = uiState.errorMessage
                    ?: stringResource(R.string.checkin_ticket_no_arrival),
            )
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
                reservation.toDoctorLabel(context),
                reservation.toScheduleLabel(context),
                reservation.toLocationLabel(context),
            ),
            highlight = stringResource(
                R.string.checkin_arrival_registered,
                ticket.checkIn.checkedInAt.toHourLabel(),
            ),
        )

        Spacer(Modifier.height(16.dp))

        InfoNote(text = stringResource(R.string.checkin_ticket_keep_note))

        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text = stringResource(R.string.checkin_ticket_view_position),
            onClick = onViewPosition,
        )

        Spacer(Modifier.height(12.dp))

        OutlineActionButton(
            text = stringResource(R.string.checkin_ticket_refresh),
            onClick = onRefresh,
        )
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
