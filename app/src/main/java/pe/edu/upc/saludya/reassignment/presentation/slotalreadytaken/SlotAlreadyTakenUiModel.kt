package pe.edu.upc.saludya.reassignment.presentation.slotalreadytaken

data class SlotAlreadyTakenUiModel(
    val title: String = "El horario ya no está disponible",
    val description: List<String> = listOf(
        "Otra persona aceptó el cupo. Tu cita original sigue confirmada.",
    ),
    val primaryText: String = "Ver mi cita",
    val secondaryText: String? = null,
)
