package pe.edu.upc.saludya.checkin.presentation.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.checkin.application.usecase.GetCheckInReservationsUseCase
import pe.edu.upc.saludya.checkin.application.usecase.GetQrCodeUseCase
import pe.edu.upc.saludya.checkin.application.usecase.RegisterArrivalUseCase
import pe.edu.upc.saludya.checkin.domain.entity.Reservation
import pe.edu.upc.saludya.checkin.infrastructure.di.CheckInModule

/**
 * ViewModel de "Código QR": carga las reservas vigentes, gestiona la selección
 * y registra la llegada del paciente.
 */
class QrCodeViewModel(
    private val getReservations: GetCheckInReservationsUseCase,
    private val getQrCode: GetQrCodeUseCase,
    private val registerArrival: RegisterArrivalUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(QrCodeUiState(isLoading = true))
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    /** Cambia la reserva seleccionada y carga su QR. */
    fun onReservationSelected(reservation: Reservation) {
        if (reservation.id == _uiState.value.selectedReservation?.id) return
        viewModelScope.launch { select(reservation) }
    }

    /**
     * Acción "Escanear QR del establecimiento".
     *
     * Versión de muestra: da el escaneo por válido y registra la llegada (`check_ins` +
     * `queue_entries`). Al integrar la cámara (CameraX + ML Kit), abrir aquí el escáner
     * y llamar a `registerArrival` solo cuando el QR leído sea el del establecimiento.
     */
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

    /** La pantalla ya navegó al ticket: se consume el evento. */
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

    companion object {
        fun factory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                QrCodeViewModel(
                    getReservations = CheckInModule.getCheckInReservations,
                    getQrCode = CheckInModule.getQrCode,
                    registerArrival = CheckInModule.registerArrival,
                )
            }
        }
    }
}
