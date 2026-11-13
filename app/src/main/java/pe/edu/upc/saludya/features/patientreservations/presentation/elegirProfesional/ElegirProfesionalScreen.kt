package pe.edu.upc.saludya.features.patientreservations.presentation.elegirProfesional

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
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaOutlinedButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaSearchField
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText
import pe.edu.upc.saludya.features.patientreservations.presentation.util.doctorDisplayName

/** R04 · Elige al profesional */
@Composable
fun ElegirProfesionalScreen(
    specialityId: Int,
    beneficiaryId: Int,
    onProfessionalClick: (doctorId: Int) -> Unit,
    onChooseByTime: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ElegirProfesionalViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(specialityId, beneficiaryId) {
        viewModel.start(specialityId, beneficiaryId)
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_professional_title),
        onBack = onBack,
        modifier = modifier
    ) {
        Text(text = state.subtitle, style = saludYaText(14, color = SaludYaColors.Muted))
        SaludYaOutlinedButton(
            text = stringResource(R.string.reservation_choose_by_time),
            onClick = onChooseByTime
        )
        SaludYaSearchField(
            label = stringResource(R.string.reservation_search_professional_label),
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            placeholder = stringResource(R.string.reservation_search_professional_placeholder)
        )

        when {
            state.isLoading -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_load_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_professionals)
                }
            )

            else -> state.visibleProfessionals.forEach { professional ->
                SaludYaInfoCard(
                    title = doctorDisplayName(professional.name, professional.lastname),
                    body = listOf(professional.detail),
                    status = stringResource(R.string.reservation_status_select),
                    onClick = { onProfessionalClick(professional.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "R04 · Elige al profesional")
@Composable
private fun ElegirProfesionalScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ElegirProfesionalScreen(
            specialityId = 0,
            beneficiaryId = 0,
            onProfessionalClick = {},
            onChooseByTime = {},
            onBack = {}
        )
    }
}
