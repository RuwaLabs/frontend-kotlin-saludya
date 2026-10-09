package pe.edu.upc.saludya.features.checkin.presentation.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.checkin.application.usecase.GetCheckInReservationsUseCase
import pe.edu.upc.saludya.features.checkin.application.usecase.GetQrCodeUseCase
import pe.edu.upc.saludya.features.checkin.application.usecase.RegisterArrivalUseCase
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import javax.inject.Inject

@HiltViewModel
class QrCodeViewModel @Inject constructor(
    private val getReservations: GetCheckInReservationsUseCase,
    private val getQrCode: GetQrCodeUseCase,
    private val registerArrival: RegisterArrivalUseCase,
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

    fun onScanEstablishmentQrClick() {
        val reservation = _uiState.value.selectedReservation ?: return
        if (_uiState.value.isRegistering) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRegistering = true, errorMessage = null) }
            try {
                registerArrival(reservation.id)
                _uiState.update { it.copy(isRegistering = false, registeredAppointmentId = reservation.id) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isRegistering = false, errorMessage = e.message ?: "No se pudo registrar tu llegada.")
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
                val first = reservations.firstOrNull()
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
        _uiState.update { it.copy(selectedReservation = reservation, qrCodeInfo = null, isLoading = true) }
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
