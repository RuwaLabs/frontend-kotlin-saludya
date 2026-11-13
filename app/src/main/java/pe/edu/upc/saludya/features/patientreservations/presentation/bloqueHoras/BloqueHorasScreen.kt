package pe.edu.upc.saludya.features.patientreservations.presentation.bloqueHoras

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import pe.edu.upc.saludya.features.patientreservations.presentation.util.doctorDisplayName
import pe.edu.upc.saludya.features.patientreservations.presentation.util.shortDateLabel

/** R05 · Elige la hora · R06 · Horarios disponibles */
@Composable
fun BloqueHorasScreen(
    specialityId: Int,
    beneficiaryId: Int,
    date: String,
    doctorId: Int?,
    onContinue: (timeSlotId: Int) -> Unit,
    onNoSlots: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BloqueHorasViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(specialityId, beneficiaryId, date, doctorId) {
        viewModel.start(specialityId, beneficiaryId, date, doctorId)
    }

    LaunchedEffect(state.isLoading, state.hasSlots, state.errorMessage) {
        if (!state.isLoading && state.errorMessage == null && !state.hasSlots) onNoSlots()
    }

    SaludYaScreen(
        title = stringResource(
            if (state.isProfessional) {
                R.string.reservation_time_title_professional
            } else {
                R.string.reservation_time_title_speciality
            }
        ),
        onBack = onBack,
        modifier = modifier
    ) {
        if (state.doctorName.isNotBlank()) {
            Text(
                text = stringResource(
                    R.string.reservation_doctor_room,
                    doctorDisplayName(state.doctorName, state.doctorLastname),
                    state.room
                ),
                style = saludYaText(14, color = SaludYaColors.Muted)
            )
        }
        Text(
            text = listOf(shortDateLabel(state.date), state.specialityName)
                .filter { it.isNotBlank() }
                .joinToString(" · "),
            style = saludYaText(14, color = SaludYaColors.Muted)
        )

        when {
            state.isLoading -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_load_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_timeslots)
                }
            )

            else -> state.slots.forEach { slot ->
                val full = slot.availableSpots <= 0
                SaludYaInfoCard(
                    title = slot.range,
                    body = listOf(slot.doctorDisplay()),
                    status = slot.statusText(),
                    statusColor = if (full) SaludYaColors.Error else SaludYaColors.Primary,
                    containerColor = if (full) SaludYaColors.ErrorBackground else SaludYaColors.White,
                    onClick = if (full) null else ({ viewModel.onSlotClick(slot.id) })
                )
            }
        }

        SaludYaPrimaryButton(
            text = stringResource(R.string.reservation_continue),
            onClick = { state.selectedSlotId?.let(onContinue) },
            enabled = state.slots.any { it.selected }
        )
    }
}

@Preview(showBackground = true, name = "R05 · Elige la hora")
@Composable
private fun BloqueHorasScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        BloqueHorasScreen(
            specialityId = 0,
            beneficiaryId = 0,
            date = "2026-10-06",
            doctorId = 1,
            onContinue = {},
            onNoSlots = {},
            onBack = {}
        )
    }
}
