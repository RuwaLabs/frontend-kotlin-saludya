package pe.edu.upc.saludya.features.checkin.presentation.qr

import androidx.compose.runtime.Immutable
import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation

/**
 * Estado inmutable de la pantalla "Código QR".
 *
 * @property reservations reservas vigentes (RESERVED / CONFIRMED) para el selector.
 * @property selectedReservation reserva seleccionada.
 * @property qrCodeInfo QR de la reserva seleccionada.
 * @property isLoading `true` mientras se cargan las reservas o el QR.
 * @property isRegistering `true` mientras se registra la llegada.
 * @property registeredAppointmentId evento de un solo uso: llegada registrada para esa cita;
 *   la pantalla navega al ticket y lo consume.
 * @property errorMessage último error a mostrar, si lo hay.
 */
@Immutable
data class QrCodeUiState(
    val reservations: List<Reservation> = emptyList(),
    val selectedReservation: Reservation? = null,
    val qrCodeInfo: QrCodeInfo? = null,
    val isLoading: Boolean = false,
    val isRegistering: Boolean = false,
    val registeredAppointmentId: Int? = null,
    val errorMessage: String? = null,
) {
    /** Las acciones solo tienen sentido con una reserva seleccionada y sin trabajo en curso. */
    val actionsEnabled: Boolean
        get() = selectedReservation != null && !isLoading && !isRegistering
}
