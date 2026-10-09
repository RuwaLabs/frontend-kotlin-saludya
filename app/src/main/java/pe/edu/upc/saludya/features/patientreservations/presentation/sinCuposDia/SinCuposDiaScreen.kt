package pe.edu.upc.saludya.features.patientreservations.presentation.sinCuposDia

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaInfoCard
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText
import pe.edu.upc.saludya.features.patientreservations.presentation.util.shortDateLabel
import pe.edu.upc.saludya.features.patientreservations.presentation.util.toHhMm

/** R09 · Sin horarios disponibles */
@Composable
fun SinCuposDiaScreen(
    specialityId: Int,
    date: String,
    onChooseAnotherDate: () -> Unit,
    onViewNextAvailability: (date: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SinCuposDiaViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(specialityId, date) {
        viewModel.start(specialityId, date)
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_no_slots_title),
        onBack = onBack,
        modifier = modifier
    ) {
        Text(
            text = listOf(state.specialityName, shortDateLabel(state.date))
                .filter { it.isNotBlank() }
                .joinToString(" · "),
            style = saludYaText(15, color = SaludYaColors.Muted)
        )
        SaludYaStateMessage(
            title = stringResource(R.string.reservation_no_slots_message_title),
            description = stringResource(R.string.reservation_no_slots_message_description)
        )
        SaludYaPrimaryButton(
            text = stringResource(R.string.reservation_choose_another_date),
            onClick = onChooseAnotherDate
        )
        state.nextDate?.let { nextDate ->
            SaludYaInfoCard(
                title = stringResource(R.string.reservation_next_availability),
                body = listOf(
                    stringResource(
                        R.string.reservation_datetime,
                        shortDateLabel(nextDate),
                        state.nextStartHour.toHhMm(),
                        state.nextEndHour.toHhMm()
                    )
                ),
                status = stringResource(R.string.reservation_view_slots_arrow),
                onClick = { onViewNextAvailability(nextDate) }
            )
        }
    }
}

@Preview(showBackground = true, name = "R09 · Sin horarios disponibles")
@Composable
private fun SinCuposDiaScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        SinCuposDiaScreen(
            specialityId = 0,
            date = "2026-10-06",
            onChooseAnotherDate = {},
            onViewNextAvailability = {},
            onBack = {}
        )
    }
}
