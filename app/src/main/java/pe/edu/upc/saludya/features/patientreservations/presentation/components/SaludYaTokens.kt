package pe.edu.upc.saludya.features.patientreservations.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SaludYaColors {
    val Text: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurface

    val Muted: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val Primary: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary

    val Soft: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.secondaryContainer

    val Background: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.background

    val White: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceContainerLowest

    val Border: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant

    val Error: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.error

    val ErrorBackground: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.errorContainer
}

/** Figma: Inter, line-height 1.45. */
@Composable
@ReadOnlyComposable
fun saludYaText(
    size: Int,
    weight: FontWeight = FontWeight.Normal,
    color: Color = SaludYaColors.Text,
    letterSpacing: Float = 0f
): TextStyle = TextStyle(
    fontSize = size.sp,
    lineHeight = (size * 1.45f).sp,
    fontWeight = weight,
    color = color,
    letterSpacing = letterSpacing.sp
)
