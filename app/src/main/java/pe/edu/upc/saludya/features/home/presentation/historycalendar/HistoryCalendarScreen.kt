package pe.edu.upc.saludya.features.home.presentation.historycalendar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.component.CalendarDayUiModel
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaBackHeader
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaDestination
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaMonthCalendar
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaOutlinedButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaPrimaryButton
import pe.edu.upc.saludya.core.desingsystem.component.SaludYaScreenLayout

@Composable
fun HistoryCalendarScreen(
    model: HistoryCalendarUiModel = HistoryCalendarUiModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPreviousMonthClick: () -> Unit = {},
    onNextMonthClick: () -> Unit = {},
    onDayClick: (CalendarDayUiModel) -> Unit = {},
    onApplyClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onDestinationClick: (SaludYaDestination) -> Unit = {},
) {
    SaludYaScreenLayout(
        modifier = modifier,
        header = { SaludYaBackHeader(title = stringResource(R.string.history_calendar_title), onBackClick = onBackClick) },
        selectedDestination = SaludYaDestination.Inicio,
        onDestinationClick = onDestinationClick,
    ) {
        SaludYaMonthCalendar(
            model = model.calendar,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onDayClick = onDayClick,
        )
        SaludYaPrimaryButton(text = stringResource(R.string.history_calendar_apply), onClick = onApplyClick)
        SaludYaOutlinedButton(text = stringResource(R.string.history_calendar_clear), onClick = onClearClick)
    }
}

@Preview(showBackground = true, widthDp = 375, heightDp = 812)
@Composable
private fun HistoryCalendarScreenPreview() {
    HistoryCalendarScreen()
}
