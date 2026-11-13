package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

val SaludYaFontFamily: FontFamily = FontFamily.Default

private fun figmaStyle(
    size: Int,
    weight: FontWeight,
    color: Color,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) = TextStyle(
    fontFamily = SaludYaFontFamily,
    fontSize = size.sp,
    fontWeight = weight,
    lineHeight = (size * 1.45f).sp,
    letterSpacing = letterSpacing,
    color = color,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

private val SemiBold = FontWeight.SemiBold
private val Regular = FontWeight.Normal

object SaludYaTextStyles {
    val ScreenTitle = figmaStyle(21, SemiBold, SaludYaColors.Text)

    val Subtitle = figmaStyle(14, Regular, SaludYaColors.Muted)
    val SectionTitle = figmaStyle(17, SemiBold, SaludYaColors.Text)
    val Highlight = figmaStyle(14, SemiBold, SaludYaColors.Primary)
    val SuccessHeadline = figmaStyle(24, SemiBold, SaludYaColors.Primary)
    val Caption = figmaStyle(12, Regular, SaludYaColors.Muted)
    val Hint = figmaStyle(13, Regular, SaludYaColors.Muted)

    val CardTitle = figmaStyle(16, SemiBold, SaludYaColors.Text)
    val CardBody = figmaStyle(13, Regular, SaludYaColors.Muted)
    val CardFooter = figmaStyle(12, SemiBold, SaludYaColors.Primary)

    val Button = figmaStyle(15, SemiBold, SaludYaColors.Surface, letterSpacing = (-0.15).sp)

    val StateTitle = figmaStyle(23, SemiBold, SaludYaColors.Text)
    val StateBody = figmaStyle(15, Regular, SaludYaColors.Muted)

    val DialogTitle = figmaStyle(22, SemiBold, SaludYaColors.Text)
    val DialogBody = figmaStyle(14, Regular, SaludYaColors.Muted)

    val NavLabelSelected = figmaStyle(10, SemiBold, SaludYaColors.Primary)
    val NavLabel = figmaStyle(10, Regular, SaludYaColors.Muted)

    val CalendarMonth = figmaStyle(17, SemiBold, SaludYaColors.Text)
    val CalendarWeekday = figmaStyle(12, SemiBold, SaludYaColors.Muted)
    val CalendarDay = figmaStyle(13, Regular, SaludYaColors.Text)
    val CalendarDayMuted = figmaStyle(13, Regular, SaludYaColors.Muted)
    val CalendarDaySelected = figmaStyle(13, SemiBold, SaludYaColors.Surface)
    val CalendarLegend = figmaStyle(12, Regular, SaludYaColors.Primary)
}
