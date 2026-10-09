package pe.edu.upc.saludya.home.presentation.filteredhistory

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.core.desingsystem.component.AppointmentCard
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaTextStyles

@Composable
fun FilteredHistoryContent(
    model: FilteredHistoryUiModel = FilteredHistoryUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onItemClick: (index: Int) -> Unit = {},
    onChangeDateClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = model.title, onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        Text(text = model.resultSummary, style = SaludYaTextStyles.Highlight)
        model.items.forEachIndexed { index, item ->
            AppointmentCard(model = item, onClick = { onItemClick(index) })
        }
        SaludYaOutlinedButton(text = model.changeDateText, onClick = onChangeDateClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun FilteredHistoryContentPreview() {
    FilteredHistoryContent()
}
