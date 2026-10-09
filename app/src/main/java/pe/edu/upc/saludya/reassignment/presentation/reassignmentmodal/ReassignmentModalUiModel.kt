package pe.edu.upc.saludya.reassignment.presentation.reassignmentmodal

data class ReassignmentModalUiModel(
    val title: String = "Reasignación de horario",
    val description: List<String> = listOf(
        "Tu cita actual: 6 oct · 09:00",
        "Nuevo horario: 6 oct · 08:30",
        "Medicina general · Dra. Ana Ríos",
        "Responde antes de las 08:05.",
    ),
    val primaryText: String = "Aceptar nuevo horario",
    val secondaryText: String? = "Rechazar oferta",
)
