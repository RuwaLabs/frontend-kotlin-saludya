package pe.edu.upc.saludya.features.checkin.presentation.called

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.features.checkin.presentation.components.AttendedDialog
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
    onAttended: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TurnCalledViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    TurnCalledContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onViewTicket = onViewTicket,
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
fun TurnCalledContent(
    uiState: TurnCalledUiState,
    onBackClick: () -> Unit,
    onViewTicket: () -> Unit,
    onRefresh: () -> Unit = {},
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    CheckInScaffold(
        title = stringResource(R.string.checkin_turn_called_title),
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
        isLoading = uiState.isLoading,
    ) {
        val ticket = uiState.ticket
        if (ticket == null) {
            InfoNote(
                text = uiState.errorMessage
                    ?: stringResource(R.string.checkin_turn_not_found),
            )
            return@CheckInScaffold
        }
        val reservation = ticket.reservation

        Spacer(Modifier.height(8.dp))

        Text(
            text = reservation.consultingRoom
                ?.let { stringResource(R.string.checkin_go_to_room, it) }
                ?: stringResource(R.string.checkin_go_to_your_room),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = stringResource(R.string.checkin_turn_code, ticket.code),
            lines = listOfNotNull(
                stringResource(
                    R.string.checkin_turn_patient_specialty,
                    reservation.patientFullName,
                    reservation.specialty,
                ),
                reservation.toDoctorLabel(context),
                ticket.queueEntry?.calledAt?.let {
                    stringResource(R.string.checkin_turn_called_at, it.toHourLabel())
                },
            ),
        )

        Spacer(Modifier.height(16.dp))

        InfoNote(
            text = stringResource(
                R.string.checkin_turn_arrival_deadline,
                toleranceLabel(uiState.postCallToleranceMinutes),
            ),
        )

        Spacer(Modifier.height(16.dp))

        OutlineActionButton(
            text = stringResource(R.string.checkin_view_ticket),
            onClick = onViewTicket,
        )

        Spacer(Modifier.height(12.dp))

        OutlineActionButton(
            text = stringResource(R.string.checkin_refresh),
            onClick = onRefresh,
        )
    }
}

@Composable
private fun toleranceLabel(minutes: Int): String =
    if (minutes == 1) {
        stringResource(R.string.checkin_one_minute)
    } else {
        stringResource(R.string.checkin_minutes, minutes)
    }

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
