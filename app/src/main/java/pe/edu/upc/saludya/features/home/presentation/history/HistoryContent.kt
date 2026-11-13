package pe.edu.upc.saludya.features.home.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout

@Composable
fun HistoryContent(
    model: HistoryUiModel = HistoryUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onItemClick: (index: Int) -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = stringResource(R.string.history_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaOutlinedButton(text = stringResource(R.string.history_filter), onClick = onFilterClick)
        model.items.forEachIndexed { index, item ->
            AppointmentCard(model = item, onClick = { onItemClick(index) })
        }
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun HistoryContentPreview() {
    HistoryContent()
}
