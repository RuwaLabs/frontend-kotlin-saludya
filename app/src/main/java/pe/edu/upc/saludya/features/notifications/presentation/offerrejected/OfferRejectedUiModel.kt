package pe.edu.upc.saludya.features.notifications.presentation.offerrejected

data class OfferRejectedUiModel(
    val title: String = "Oferta rechazada",
    val description: List<String> = listOf(
        "Conservamos tu horario original: 6 oct a las 09:00.",
    ),
    val primaryText: String = "Ver mi cita",
    val secondaryText: String? = null,
)
