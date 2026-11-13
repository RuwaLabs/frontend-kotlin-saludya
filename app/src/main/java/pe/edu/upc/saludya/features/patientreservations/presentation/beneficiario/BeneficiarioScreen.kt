package pe.edu.upc.saludya.features.patientreservations.presentation.beneficiario

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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

data class BeneficiarioUi(
    val id: Int,
    val name: String,
    val titular: Boolean,
    val maskedDni: String
)

/** R02 · ¿Para quién es la cita? */
@Composable
fun BeneficiarioScreen(
    onContinue: (beneficiaryId: Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BeneficiarioViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    SaludYaScreen(
        title = stringResource(R.string.reservation_beneficiary_title),
        onBack = onBack,
        modifier = modifier
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                color = SaludYaColors.Primary,
                modifier = Modifier.padding(top = 24.dp)
            )

            state.errorMessage != null -> SaludYaStateMessage(
                title = stringResource(R.string.reservation_error_load_generic_title),
                description = state.errorMessage.ifBlank {
                    stringResource(R.string.reservation_error_beneficiaries)
                }
            )

            else -> state.beneficiaries.forEach { beneficiary ->
                SaludYaInfoCard(
                    title = beneficiary.name,
                    body = listOf(
                        stringResource(
                            if (beneficiary.titular) {
                                R.string.reservation_beneficiary_titular
                            } else {
                                R.string.reservation_beneficiary_minor
                            },
                            beneficiary.maskedDni
                        )
                    ),
                    status = if (beneficiary.id == state.selectedId) {
                        stringResource(R.string.reservation_status_selected)
                    } else {
                        stringResource(R.string.reservation_status_select)
                    },
                    onClick = { viewModel.onSelect(beneficiary) }
                )
            }
        }

        SaludYaPrimaryButton(
            text = stringResource(R.string.reservation_continue),
            onClick = { state.selectedId?.let(onContinue) },
            enabled = state.selectedId != null
        )
    }
}

@Preview(showBackground = true, name = "R02 · ¿Para quién es la cita?")
@Composable
private fun BeneficiarioScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        BeneficiarioScreen(onContinue = {}, onBack = {})
    }
}
