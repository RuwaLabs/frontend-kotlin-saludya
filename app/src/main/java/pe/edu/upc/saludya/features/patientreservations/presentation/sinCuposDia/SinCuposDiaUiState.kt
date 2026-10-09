package pe.edu.upc.saludya.features.patientreservations.presentation.sinCuposDia

data class SinCuposDiaUiState(
    val isLoading: Boolean = true,
    val specialityName: String = "",
    val date: String = "",
    val nextDate: String? = null,
    val nextStartHour: String = "",
    val nextEndHour: String = ""
)
