package pe.edu.upc.saludya.features.notifications.presentation.reassignmentsuccess

data class ReassignmentSuccessUiModel(
    val title: String = "Reasignación exitosa",
    val description: List<String> = listOf(
        "Tu cita ahora es el 6 oct a las 08:30.",
    ),
    val primaryText: String = "Ver detalle",
    val secondaryText: String? = null,
)
