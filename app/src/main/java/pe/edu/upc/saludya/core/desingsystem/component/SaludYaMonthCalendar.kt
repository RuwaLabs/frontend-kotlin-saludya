package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class CalendarDayType { Empty, Normal, Muted, Selected }

data class CalendarDayUiModel(
    val label: String,
    val type: CalendarDayType = CalendarDayType.Normal,
)

data class CalendarUiModel(
    val monthLabel: String = "Octubre 2026",
    val weekdays: List<String> = listOf("L", "M", "M", "J", "V", "S", "D"),
    val weeks: List<List<CalendarDayUiModel>> = octubre2026Weeks(),
    val legend: String = "● Disponible     6 Fecha seleccionada",
)

private fun octubre2026Weeks(): List<List<CalendarDayUiModel>> {
    fun empty() = CalendarDayUiModel(" ", CalendarDayType.Empty)
    fun day(n: Int, type: CalendarDayType = CalendarDayType.Normal) = CalendarDayUiModel(n.toString(), type)
    return listOf(
        listOf(empty(), empty(), empty(), day(1, CalendarDayType.Muted), day(2, CalendarDayType.Muted), day(3), day(4)),
        listOf(day(5), day(6, CalendarDayType.Selected), day(7), day(8), day(9), day(10), day(11)),
        (12..18).map { day(it) },
        (19..25).map { day(it) },
        (26..31).map { day(it) } + empty(),
    )
}

@Composable
fun SaludYaMonthCalendar(
    model: CalendarUiModel,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDayClick: (CalendarDayUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SaludYaColors.Surface)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "‹",
                style = SaludYaTextStyles.CalendarMonth,
                modifier = Modifier.clickable(onClick = onPreviousMonthClick),
            )
            Spacer(Modifier.width(24.dp))
            Text(text = model.monthLabel, style = SaludYaTextStyles.CalendarMonth)
            Spacer(Modifier.width(24.dp))
            Text(
                text = "›",
                style = SaludYaTextStyles.CalendarMonth,
                modifier = Modifier.clickable(onClick = onNextMonthClick),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            model.weekdays.forEach { weekday ->
                Text(
                    text = weekday,
                    style = SaludYaTextStyles.CalendarWeekday,
                    modifier = Modifier.width(40.dp),
                )
            }
        }

        model.weeks.forEach { week ->
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                week.forEach { day ->
                    CalendarDayCell(day = day, onClick = { onDayClick(day) })
                }
            }
        }

        Text(text = model.legend, style = SaludYaTextStyles.CalendarLegend)
    }
}

@Composable
private fun CalendarDayCell(
    day: CalendarDayUiModel,
    onClick: () -> Unit,
) {
    val background = if (day.type == CalendarDayType.Selected) SaludYaColors.Primary else Color.Transparent
    val textStyle = when (day.type) {
        CalendarDayType.Selected -> SaludYaTextStyles.CalendarDaySelected
        CalendarDayType.Muted -> SaludYaTextStyles.CalendarDayMuted
        else -> SaludYaTextStyles.CalendarDay
    }
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable(enabled = day.type != CalendarDayType.Empty, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.label,
            style = textStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
