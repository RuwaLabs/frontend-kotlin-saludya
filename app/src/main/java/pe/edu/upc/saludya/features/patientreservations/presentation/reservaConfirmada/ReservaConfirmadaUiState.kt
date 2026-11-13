package pe.edu.upc.saludya.features.patientreservations.presentation.reservaConfirmada

import pe.edu.upc.saludya.features.patientreservations.presentation.components.ReservaSummaryUi

data class ReservaConfirmadaUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val summary: ReservaSummaryUi? = null,
    val bookingCode: String = ""
)
