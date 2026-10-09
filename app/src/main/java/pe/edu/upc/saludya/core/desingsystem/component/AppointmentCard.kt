package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class AppointmentUiModel(
    val specialty: String,
    val details: List<String>,
    val status: String,
)

@Composable
fun AppointmentCard(
    model: AppointmentUiModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    SaludYaInfoCard(
        model = InfoCardUiModel(
            title = model.specialty,
            lines = model.details,
            footer = model.status,
        ),
        modifier = modifier,
        onClick = onClick,
    )
}
