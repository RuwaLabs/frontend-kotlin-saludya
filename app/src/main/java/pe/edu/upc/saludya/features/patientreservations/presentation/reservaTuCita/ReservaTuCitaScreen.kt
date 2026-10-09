package pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaSearchField
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage
import pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita.components.SpecialityCard

/** R01 · Reserva tu cita */
@Composable
fun ReservaTuCitaScreen(
    onSpecialityClick: (Int) -> Unit,
    onNoResults: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservaTuCitaViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(state.navigateToNoResults) {
        if (state.navigateToNoResults) {
            onNoResults(state.query)
            viewModel.onNoResultsHandled()
        }
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_tucita_title),
        onBack = onBack,
        modifier = modifier
    ) {
        SaludYaSearchField(
            label = stringResource(R.string.reservation_search_speciality_label),
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            placeholder = stringResource(R.string.reservation_search_speciality_placeholder)
        )

        when {
            state.isLoading -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_load_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_specialities)
                }
            )

            else -> state.visibleSpecialities.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { speciality ->
                        SpecialityCard(
                            speciality = speciality,
                            onClick = { onSpecialityClick(speciality.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "R01 · Reserva tu cita")
@Composable
private fun ReservaTuCitaScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ReservaTuCitaScreen(onSpecialityClick = {}, onNoResults = {}, onBack = {})
    }
}
