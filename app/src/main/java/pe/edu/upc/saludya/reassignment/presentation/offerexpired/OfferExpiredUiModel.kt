package pe.edu.upc.saludya.reassignment.presentation.offerexpired

data class OfferExpiredUiModel(
    val title: String = "La oferta venció",
    val description: List<String> = listOf(
        "El plazo para aceptar terminó. Tu reserva original sigue confirmada.",
    ),
    val primaryText: String = "Ver mi cita",
    val secondaryText: String? = null,
)
