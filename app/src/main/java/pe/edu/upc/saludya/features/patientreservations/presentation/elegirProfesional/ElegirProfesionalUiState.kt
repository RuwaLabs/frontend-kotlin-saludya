package pe.edu.upc.saludya.features.patientreservations.presentation.elegirProfesional

data class ProfesionalUi(
    val id: Int,
    val name: String,
    val lastname: String,
    val detail: String
)

data class ElegirProfesionalUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val subtitle: String = "",
    val professionals: List<ProfesionalUi> = emptyList(),
    val query: String = ""
) {
    val visibleProfessionals: List<ProfesionalUi>
        get() = if (query.isBlank()) {
            professionals
        } else {
            professionals.filter {
                "$it.name $it.lastname".contains(query.trim(), ignoreCase = true)
            }
        }
}
