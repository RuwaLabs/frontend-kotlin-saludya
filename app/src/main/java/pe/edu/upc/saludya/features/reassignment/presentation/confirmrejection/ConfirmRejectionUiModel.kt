package pe.edu.upc.saludya.features.reassignment.presentation.confirmrejection

data class ConfirmRejectionUiModel(
    val title: String = "¿Rechazar esta oferta?",
    val description: List<String> = listOf(
        "Conservarás tu cita del 6 oct a las 09:00.",
    ),
    val primaryText: String = "Rechazar oferta",
    val secondaryText: String? = "Volver",
)
