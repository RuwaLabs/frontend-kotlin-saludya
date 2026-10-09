package pe.edu.upc.saludya.core.desingsystem.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

object SaludYaIcons {

    val Bell: ImageVector by lazy {
        strokeIcon(
            name = "Bell", frame = 24f, offsetX = 4f, offsetY = 3f,
            path = "M 6 18 L 10 18 M 2 13 L 2 6 C 2 4.409 2.632 2.883 3.757 1.757 C 4.883 0.632 6.409 0 8 0 " +
                "C 9.591 0 11.117 0.632 12.243 1.757 C 13.368 2.883 14 4.409 14 6 L 14 13 L 16 15 L 0 15 L 2 13 Z",
        )
    }

    val Back: ImageVector by lazy {
        strokeIcon("Back", 24f, 8f, 5f, "M 7 0 L 0 7 L 7 14")
    }

    val Home: ImageVector by lazy {
        strokeIcon(
            "Home", 22f, 2.75f, 2.75f,
            "M 0 6.417 L 8.25 0 L 16.5 6.417 M 1.833 5.5 L 1.833 16.5 L 14.667 16.5 L 14.667 5.5 " +
                "M 5.5 16.5 L 5.5 10.083 L 11 10.083 L 11 16.5",
        )
    }

    val Calendar: ImageVector by lazy {
        strokeIcon(
            "Calendar", 22f, 3.667f, 2.75f,
            "M 3.667 0 L 3.667 3.667 M 11 0 L 11 3.667 M 0 6.417 L 14.667 6.417 " +
                "M 3.667 10.083 L 5.5 10.083 M 9.167 10.083 L 11 10.083 M 3.667 13.75 L 5.5 13.75 " +
                "M 0 1.833 L 14.667 1.833 L 14.667 16.5 L 0 16.5 L 0 1.833 Z",
        )
    }

    val Qr: ImageVector by lazy {
        strokeIcon(
            "Qr", 22f, 2.75f, 2.75f,
            "M 14.667 14.667 L 16.5 14.667 L 16.5 16.5 L 14.667 16.5 M 14.667 10.083 L 14.667 12.833 " +
                "M 10.083 15.583 L 12.833 15.583 M 0 0 L 5.5 0 L 5.5 5.5 L 0 5.5 L 0 0 Z " +
                "M 11 0 L 16.5 0 L 16.5 5.5 L 11 5.5 L 11 0 Z M 0 11 L 5.5 11 L 5.5 16.5 L 0 16.5 L 0 11 Z " +
                "M 11 11 L 12.833 11 L 12.833 12.833 L 11 12.833 L 11 11 Z",
        )
    }

    val Settings: ImageVector by lazy {
        strokeIcon(
            "Settings", 22f, 1.833f, 1.833f,
            "M 9.167 0 L 9.167 2.75 M 9.167 15.583 L 9.167 18.333 M 0 9.167 L 2.75 9.167 " +
                "M 15.583 9.167 L 18.333 9.167 M 2.75 2.75 L 4.583 4.583 M 13.75 13.75 L 15.583 15.583 " +
                "M 2.75 15.583 L 4.583 13.75 M 13.75 4.583 L 15.583 2.75 " +
                "M 9.167 5.5 C 8.194 5.5 7.262 5.886 6.574 6.574 C 5.886 7.262 5.5 8.194 5.5 9.167 " +
                "C 5.5 10.139 5.886 11.072 6.574 11.759 C 7.262 12.447 8.194 12.833 9.167 12.833 " +
                "C 10.139 12.833 11.072 12.447 11.759 11.759 C 12.447 11.072 12.833 10.139 12.833 9.167 " +
                "C 12.833 8.194 12.447 7.262 11.759 6.574 C 11.072 5.886 10.139 5.5 9.167 5.5 Z",
        )
    }

    val Cross: ImageVector by lazy {
        strokeIcon(
            "Cross", 48f, 6f, 6f,
            "M 12 0 L 24 0 L 24 12 L 36 12 L 36 24 L 24 24 L 24 36 L 12 36 L 12 24 L 0 24 L 0 12 " +
                "L 12 12 L 12 0 Z",
        )
    }

    private fun strokeIcon(
        name: String,
        frame: Float,
        offsetX: Float,
        offsetY: Float,
        path: String,
    ): ImageVector = ImageVector.Builder(
        name = name,
        defaultWidth = frame.dp,
        defaultHeight = frame.dp,
        viewportWidth = frame,
        viewportHeight = frame,
    ).apply {
        addGroup(translationX = offsetX, translationY = offsetY)
        addPath(
            pathData = addPathNodes(path),
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.7f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
        clearGroup()
    }.build()
}
