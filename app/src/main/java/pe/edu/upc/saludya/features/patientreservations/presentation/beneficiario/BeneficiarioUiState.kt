package pe.edu.upc.saludya.features.patientreservations.presentation.beneficiario

data class BeneficiarioUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val beneficiaries: List<BeneficiarioUi> = emptyList(),
    val selectedId: Int? = null
)
