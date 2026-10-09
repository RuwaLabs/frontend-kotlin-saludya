package pe.edu.upc.saludya.features.checkin.presentation.qr

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInScaffold
import pe.edu.upc.saludya.features.checkin.presentation.components.InfoNote
import pe.edu.upc.saludya.features.checkin.presentation.components.OutlineActionButton
import pe.edu.upc.saludya.features.checkin.presentation.components.PrimaryButton
import pe.edu.upc.saludya.features.checkin.presentation.components.ReservationSelector
import pe.edu.upc.saludya.features.checkin.presentation.components.CheckInPreviewData
import pe.edu.upc.saludya.features.checkin.presentation.qr.components.QrDisplayCard

/**
 * Pantalla "Código QR" — punto de entrada con estado.
 *
 * @param onBackClick navegación hacia atrás.
 * @param onViewReservationDetail abre el detalle de la cita (`appointments.id`).
 * @param onArrivalRegistered la llegada quedó registrada; navegar al ticket de esa cita.
 * @param onBottomNavItemSelected navegación entre pestañas de la barra inferior.
 */
@Composable
fun QrCodeScreen(
    onBackClick: () -> Unit,
    onViewReservationDetail: (appointmentId: Int) -> Unit,
    onArrivalRegistered: (appointmentId: Int) -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.registeredAppointmentId) {
        uiState.registeredAppointmentId?.let { appointmentId ->
            viewModel.onArrivalNavigated()
            onArrivalRegistered(appointmentId)
        }
    }

    QrCodeContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onReservationSelected = viewModel::onReservationSelected,
        onViewReservationDetail = onViewReservationDetail,
        onScanEstablishmentQr = viewModel::onScanEstablishmentQrClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    )
}

/** Contenido sin estado de la pantalla. */
@Composable
fun QrCodeContent(
    uiState: QrCodeUiState,
    onBackClick: () -> Unit,
    onReservationSelected: (Reservation) -> Unit,
    onViewReservationDetail: (appointmentId: Int) -> Unit,
    onScanEstablishmentQr: () -> Unit,
    onBottomNavItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    CheckInScaffold(
        title = "Código QR",
        onBackClick = onBackClick,
        onBottomNavItemSelected = onBottomNavItemSelected,
        modifier = modifier,
    ) {
        ReservationSelector(
            reservations = uiState.reservations,
            selected = uiState.selectedReservation,
            onReservationSelected = onReservationSelected,
        )

        Spacer(Modifier.height(16.dp))

        QrDisplayCard(
            qrCodeInfo = uiState.qrCodeInfo,
            isLoading = uiState.isLoading,
        )

        Spacer(Modifier.height(20.dp))

        InfoNote(
            text = "Al llegar, escanea el QR del establecimiento. " +
                "Si admisión lo solicita, presenta este código.",
        )

        if (uiState.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = uiState.errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(Modifier.height(20.dp))

        OutlineActionButton(
            text = "Ver detalle de reserva",
            enabled = uiState.actionsEnabled,
            onClick = { uiState.selectedReservation?.let { onViewReservationDetail(it.id) } },
        )

        Spacer(Modifier.height(12.dp))

        PrimaryButton(
            text = "Escanear QR del establecimiento",
            enabled = uiState.actionsEnabled,
            onClick = onScanEstablishmentQr,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun QrCodeContentPreview() {
    // Envolver con el tema del proyecto (p. ej. SaludYaTheme) para ver los colores reales.
    MaterialTheme {
        QrCodeContent(
            uiState = QrCodeUiState(
                reservations = listOf(CheckInPreviewData.reservation),
                selectedReservation = CheckInPreviewData.reservation,
                qrCodeInfo = CheckInPreviewData.qrCode,
            ),
            onBackClick = {},
            onReservationSelected = {},
            onViewReservationDetail = {},
            onScanEstablishmentQr = {},
            onBottomNavItemSelected = {},
        )
    }
}
