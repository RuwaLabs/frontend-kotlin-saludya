package pe.edu.upc.saludya.features.patientreservations.presentation.elegirFecha

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaColors
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaPrimaryButton
import pe.edu.upc.saludya.features.patientreservations.presentation.components.SaludYaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.components.saludYaText
import pe.edu.upc.saludya.features.patientreservations.presentation.util.monthLabel

private val WeekDays = listOf("L", "M", "M", "J", "V", "S", "D")

/** R03 · Elige la fecha */
@Composable
fun ElegirFechaScreen(
    specialityId: Int,
    beneficiaryId: Int,
    onContinue: (date: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNoSlots: (date: String) -> Unit = {},
    viewModel: ElegirFechaViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(specialityId, beneficiaryId) {
        viewModel.start(specialityId, beneficiaryId)
    }

    SaludYaScreen(
        title = stringResource(R.string.reservation_date_title),
        onBack = onBack,
        modifier = modifier
    ) {
        Text(text = state.subtitle, style = saludYaText(14, color = SaludYaColors.Muted))

        CalendarCard(
            monthLabel = if (state.month in 1..12) monthLabel(state.month, state.year) else "",
            firstDayOffset = state.firstDayOffset,
            daysInMonth = state.daysInMonth,
            enabledDays = state.enabledDays,
            selectedDay = state.selectedDay,
            onDayClick = viewModel::onDayClick,
            onPreviousMonth = viewModel::onPreviousMonth,
            onNextMonth = viewModel::onNextMonth
        )

        if (state.isLoading) {
            Text(
                text = stringResource(R.string.reservation_date_loading),
                style = saludYaText(12, color = SaludYaColors.Muted)
            )
        }

        SaludYaPrimaryButton(
            text = stringResource(R.string.reservation_view_slots),
            onClick = {
                val date = state.selectedDate
                if (date != null) {
                    if (state.selectedDay in state.enabledDays) {
                        onContinue(date)
                    } else {
                        onNoSlots(date)
                    }
                }
            },
            enabled = state.selectedDay != null
        )
    }
}

@Composable
private fun CalendarCard(
    monthLabel: String,
    firstDayOffset: Int,
    daysInMonth: Int,
    enabledDays: Set<Int>,
    selectedDay: Int?,
    onDayClick: (Int) -> Unit,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SaludYaColors.White)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "‹",
                style = saludYaText(17, FontWeight.SemiBold),
                modifier = Modifier.clickable(onClick = onPreviousMonth)
            )
            Text(
                text = "      $monthLabel      ",
                style = saludYaText(17, FontWeight.SemiBold)
            )
            Text(
                text = "›",
                style = saludYaText(17, FontWeight.SemiBold),
                modifier = Modifier.clickable(onClick = onNextMonth)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            WeekDays.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.width(40.dp),
                    style = saludYaText(12, FontWeight.SemiBold, SaludYaColors.Muted)
                )
            }
        }

        val cells: List<Int?> = List(firstDayOffset) { null } + (1..daysInMonth).toList()
        cells.chunked(7).forEach { week ->
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                week.forEach { day -> DayCell(day, enabledDays, selectedDay, onDayClick) }
                repeat(7 - week.size) { Box(modifier = Modifier.width(40.dp).height(32.dp)) }
            }
        }

        val legend = if (selectedDay == null) {
            stringResource(R.string.reservation_date_legend_available)
        } else {
            stringResource(R.string.reservation_date_legend_available) + "     " +
                stringResource(R.string.reservation_date_selected, selectedDay)
        }
        Text(text = legend, style = saludYaText(12, color = SaludYaColors.Primary))
    }
}

@Composable
private fun DayCell(
    day: Int?,
    enabledDays: Set<Int>,
    selectedDay: Int?,
    onDayClick: (Int) -> Unit
) {
    val base = Modifier
        .width(40.dp)
        .height(32.dp)
        .clip(RoundedCornerShape(8.dp))

    if (day == null) {
        Box(modifier = base)
        return
    }

    val isSelected = day == selectedDay
    val isEnabled = day in enabledDays

    Box(
        modifier = base
            .background(if (isSelected) SaludYaColors.Primary else SaludYaColors.White)
            .clickable { onDayClick(day) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            textAlign = TextAlign.Center,
            style = saludYaText(
                size = 13,
                weight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    isSelected -> SaludYaColors.White
                    isEnabled -> SaludYaColors.Text
                    else -> SaludYaColors.Muted
                }
            )
        )
    }
}

@Preview(showBackground = true, name = "R03 · Elige la fecha")
@Composable
private fun ElegirFechaScreenPreview() {
    SaludYaTheme(dynamicColor = false) {
        ElegirFechaScreen(specialityId = 0, beneficiaryId = 0, onContinue = {}, onBack = {})
    }
}
