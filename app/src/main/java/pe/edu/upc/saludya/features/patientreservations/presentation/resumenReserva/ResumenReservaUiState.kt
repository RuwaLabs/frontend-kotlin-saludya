package pe.edu.upc.saludya.features.patientreservations.presentation.resumenReserva

import pe.edu.upc.saludya.features.patientreservations.presentation.components.ReservaSummaryUi

enum class ReservaDialog { NONE, CONFIRMAR, CUPO_OCUPADO, CRUCE }

enum class ResumenReservaError {
    SLOT_NOT_FOUND,
    SUMMARY,
    BOOKING
}

data class ResumenReservaUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val errorType: ResumenReservaError? = null,
    val summary: ReservaSummaryUi? = null,
    val beneficiaryName: String = "",
    val dialog: ReservaDialog = ReservaDialog.NONE,
    val isBooking: Boolean = false,
    val bookedAppointmentId: Int? = null
)
