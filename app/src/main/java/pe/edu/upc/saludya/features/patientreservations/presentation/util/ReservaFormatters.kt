package pe.edu.upc.saludya.features.patientreservations.presentation.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@StringRes
private fun monthRes(month: Int): Int = when (month) {
    1 -> R.string.reservation_month_january
    2 -> R.string.reservation_month_february
    3 -> R.string.reservation_month_march
    4 -> R.string.reservation_month_april
    5 -> R.string.reservation_month_may
    6 -> R.string.reservation_month_june
    7 -> R.string.reservation_month_july
    8 -> R.string.reservation_month_august
    9 -> R.string.reservation_month_september
    10 -> R.string.reservation_month_october
    11 -> R.string.reservation_month_november
    else -> R.string.reservation_month_december
}

@StringRes
private fun weekdayRes(dayOfWeek: Int): Int = when (dayOfWeek) {
    Calendar.MONDAY -> R.string.reservation_weekday_monday
    Calendar.TUESDAY -> R.string.reservation_weekday_tuesday
    Calendar.WEDNESDAY -> R.string.reservation_weekday_wednesday
    Calendar.THURSDAY -> R.string.reservation_weekday_thursday
    Calendar.FRIDAY -> R.string.reservation_weekday_friday
    Calendar.SATURDAY -> R.string.reservation_weekday_saturday
    else -> R.string.reservation_weekday_sunday
}

/** "Octubre 2026" a partir del mes (1–12) y el año. */
@Composable
fun monthLabel(month: Int, year: Int): String = "${stringResource(monthRes(month))} $year"

/** "Mar 6 oct 2026" a partir de una fecha ISO (yyyy-MM-dd). */
@Composable
fun shortDateLabel(date: String): String {
    val parsed = runCatching {
        SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)
    }.getOrNull() ?: return date
    val calendar = Calendar.getInstance().apply { time = parsed }
    val day = stringResource(weekdayRes(calendar.get(Calendar.DAY_OF_WEEK)))
    val month = stringResource(monthRes(calendar.get(Calendar.MONTH) + 1)).lowercase().take(3)
    return "$day ${calendar.get(Calendar.DAY_OF_MONTH)} $month ${calendar.get(Calendar.YEAR)}"
}

fun String.toHhMm(): String = if (length >= 5) substring(0, 5) else this
