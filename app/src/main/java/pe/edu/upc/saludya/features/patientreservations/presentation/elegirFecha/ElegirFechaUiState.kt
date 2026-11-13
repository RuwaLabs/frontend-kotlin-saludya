package pe.edu.upc.saludya.features.patientreservations.presentation.elegirFecha

data class ElegirFechaUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val specialityName: String = "",
    val beneficiaryName: String = "",
    val year: Int = 0,
    val month: Int = 0,
    val firstDayOffset: Int = 0,
    val daysInMonth: Int = 0,
    val enabledDays: Set<Int> = emptySet(),
    val selectedDay: Int? = null
) {
    val subtitle: String
        get() = listOf(specialityName, beneficiaryName)
            .filter { it.isNotBlank() }
            .joinToString(" · ")

    val selectedDate: String?
        get() = selectedDay?.let { String.format("%04d-%02d-%02d", year, month, it) }
}
