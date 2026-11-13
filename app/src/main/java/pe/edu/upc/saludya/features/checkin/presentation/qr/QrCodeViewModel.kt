package pe.edu.upc.saludya.features.checkin.presentation.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.checkin.application.usecase.GetCheckInReservationsUseCase
import pe.edu.upc.saludya.features.checkin.application.usecase.GetQrCodeUseCase
import pe.edu.upc.saludya.features.checkin.application.usecase.ObserveAttendanceTicketUseCase
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val getReservations: GetCheckInReservationsUseCase,
    private val getQrCode: GetQrCodeUseCase,
    private val observeTicket: ObserveAttendanceTicketUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QrCodeUiState(isLoading = true))
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    fun onReservationSelected(reservation: Reservation) {
        if (reservation.id == _uiState.value.selectedReservation?.id) return
        viewModelScope.launch { select(reservation) }
    }

    fun onCheckTicketClick() {
        val state = _uiState.value
        val reservation = state.selectedReservation ?: return
        if (state.isCheckingTicket || !state.sameDay) return
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingTicket = true, errorMessage = null) }
            try {
                val ticket = observeTicket(reservation.id).first()
                _uiState.update {
                    it.copy(
                        isCheckingTicket = false,
                        registeredAppointmentId = if (ticket != null) reservation.id else null,
                        errorMessage = if (ticket == null) "Aún no hay llegada registrada por admisión." else null,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isCheckingTicket = false, errorMessage = e.message ?: "No se pudo consultar tu ticket.")
                }
            }
        }
    }

    fun onArrivalNavigated() {
        _uiState.update { it.copy(registeredAppointmentId = null) }
    }

    private fun loadReservations() {
        viewModelScope.launch {
            try {
                val reservations = getReservations()
                _uiState.update { it.copy(reservations = reservations) }
                val today = LocalDate.now(ZoneId.of("America/Lima"))
                val first = reservations.firstOrNull { it.date == today } ?: reservations.firstOrNull()
                if (first != null) select(first) else _uiState.update { it.copy(isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "No se pudieron cargar tus reservas.")
                }
            }
        }
    }

    private suspend fun select(reservation: Reservation) {
        val today = LocalDate.now(ZoneId.of("America/Lima"))
        val sameDay = reservation.date == today
        _uiState.update {
            it.copy(
                selectedReservation = reservation,
                qrCodeInfo = null,
                sameDay = sameDay,
                isLoading = sameDay,
                errorMessage = if (sameDay) null else
                    "El código y la cola solo están disponibles el día de la cita (${reservation.date}).",
            )
        }
        if (!sameDay) return
        try {
            val qrCode = getQrCode(reservation.id)
            _uiState.update { it.copy(qrCodeInfo = qrCode, isLoading = false, errorMessage = null) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, errorMessage = e.message ?: "No se pudo cargar el código QR.")
            }
        }
    }
}
