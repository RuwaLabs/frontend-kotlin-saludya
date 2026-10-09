package pe.edu.upc.saludya.features.patientreservations.presentation.busquedaSinResultados

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaSearchField
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaStateMessage

/** R10 · Búsqueda sin resultados */
@Composable
fun BusquedaSinResultadosScreen(
    initialQuery: String,
    onClearSearch: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BusquedaSinResultadosViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(initialQuery) {
        viewModel.start(initialQuery)
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
        SaludYaStateMessage(
            title = stringResource(R.string.reservation_no_results_title),
            description = stringResource(R.string.reservation_no_results_description)
        )
        SaludYaPrimaryButton(
            text = stringResource(R.string.reservation_clear_search),
            onClick = onClearSearch
        )
    }
}

@Preview(showBackground = true, name = "R10 · Búsqueda sin resultados")
@Composable
private fun BusquedaSinResultadosScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        BusquedaSinResultadosScreen(initialQuery = "", onClearSearch = {}, onBack = {})
    }
}
