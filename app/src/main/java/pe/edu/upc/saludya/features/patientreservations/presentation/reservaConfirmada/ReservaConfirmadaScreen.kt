package pe.edu.upc.saludya.features.patientreservations.presentation.reservaConfirmada

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaReservaSummaryCard
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText

/** R08 · Reserva confirmada */
@Composable
fun ReservaConfirmadaScreen(
    appointmentId: Int,
    onViewAppointments: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservaConfirmadaViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(appointmentId) {
        viewModel.start(appointmentId)
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_confirmed_title),
        onBack = onBack,
        modifier = modifier
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_load_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_load_booking)
                }
            )

            state.summary != null -> {
                Text(
                    text = stringResource(R.string.reservation_confirmed_message),
                    style = saludYaText(24, FontWeight.SemiBold, SaludYaColors.Primary)
                )
                SaludYaReservaSummaryCard(
                    summary = state.summary,
                    status = stringResource(R.string.reservation_status_confirmed)
                )
                Text(
                    text = stringResource(R.string.reservation_confirmed_code, state.bookingCode),
                    style = saludYaText(14, color = SaludYaColors.Muted)
                )
                SaludYaPrimaryButton(
                    text = stringResource(R.string.reservation_view_my_appointments),
                    onClick = onViewAppointments
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "R08 · Reserva confirmada")
@Composable
private fun ReservaConfirmadaScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ReservaConfirmadaScreen(appointmentId = 0, onViewAppointments = {}, onBack = {})
    }
}
