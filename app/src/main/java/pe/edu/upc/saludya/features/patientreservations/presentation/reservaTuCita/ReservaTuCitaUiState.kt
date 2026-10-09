package pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita

import pe.edu.upc.saludya.features.patientreservations.domain.model.Speciality

data class ReservaTuCitaUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val specialities: List<Speciality> = emptyList(),
    val query: String = "",
    val navigateToNoResults: Boolean = false
) {
    val visibleSpecialities: List<Speciality>
        get() = if (query.isBlank()) {
            specialities
        } else {
            specialities.filter { it.name.contains(query.trim(), ignoreCase = true) }
        }
}
