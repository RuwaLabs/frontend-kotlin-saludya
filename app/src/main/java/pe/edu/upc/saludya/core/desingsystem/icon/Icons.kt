package pe.edu.upc.saludya.core.desingsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val visibility: ImageVector
    get() {
        if (_visibility != null) {
            return _visibility!!
        }
        _visibility =
            ImageVector.Builder(
                name = "visibility",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(15.19f, 14.69f)
                        quadTo(16.5f, 13.38f, 16.5f, 11.5f)
                        reflectiveQuadTo(15.19f, 8.31f)
                        reflectiveQuadTo(12f, 7f)
                        reflectiveQuadTo(8.81f, 8.31f)
                        reflectiveQuadTo(7.5f, 11.5f)
                        reflectiveQuadToRelative(1.31f, 3.19f)
                        reflectiveQuadTo(12f, 16f)
                        reflectiveQuadToRelative(3.19f, -1.31f)
                        close()
                        moveToRelative(-5.1f, -1.28f)
                        quadTo(9.3f, 12.63f, 9.3f, 11.5f)
                        reflectiveQuadTo(10.09f, 9.59f)
                        reflectiveQuadTo(12f, 8.8f)
                        reflectiveQuadToRelative(1.91f, 0.79f)
                        quadToRelative(0.79f, 0.79f, 0.79f, 1.91f)
                        reflectiveQuadToRelative(-0.79f, 1.91f)
                        reflectiveQuadTo(12f, 14.2f)
                        reflectiveQuadTo(10.09f, 13.41f)
                        close()
                        moveTo(5.35f, 16.96f)
                        quadTo(2.35f, 14.93f, 1f, 11.5f)
                        quadTo(2.35f, 8.07f, 5.35f, 6.04f)
                        reflectiveQuadTo(12f, 4f)
                        reflectiveQuadToRelative(6.65f, 2.04f)
                        reflectiveQuadTo(23f, 11.5f)
                        quadToRelative(-1.35f, 3.42f, -4.35f, 5.46f)
                        reflectiveQuadTo(12f, 19f)
                        reflectiveQuadTo(5.35f, 16.96f)
                        close()
                        moveTo(12f, 11.5f)
                        close()
                        moveToRelative(5.19f, 4.01f)
                        quadTo(19.55f, 14.02f, 20.8f, 11.5f)
                        quadTo(19.55f, 8.98f, 17.19f, 7.49f)
                        reflectiveQuadTo(12f, 6f)
                        quadTo(9.18f, 6f, 6.81f, 7.49f)
                        reflectiveQuadTo(3.2f, 11.5f)
                        quadToRelative(1.25f, 2.52f, 3.61f, 4.01f)
                        reflectiveQuadTo(12f, 17f)
                        reflectiveQuadToRelative(5.19f, -1.49f)
                        close()
                    }
                }
                .build()
        return _visibility!!
    }

private var _visibility: ImageVector? = null


@Suppress("CheckReturnValue")
val visibilityOff: ImageVector
    get() {
        if (_visibilityOff != null) {
            return _visibilityOff!!
        }
        _visibilityOff =
            ImageVector.Builder(
                name = "visibilityOff",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(16.1f, 13.3f)
                        lineTo(14.65f, 11.85f)
                        quadToRelative(0.22f, -1.18f, -0.67f, -2.2f)
                        quadTo(13.08f, 8.63f, 11.65f, 8.85f)
                        lineTo(10.2f, 7.4f)
                        quadTo(10.63f, 7.2f, 11.06f, 7.1f)
                        reflectiveQuadTo(12f, 7f)
                        quadToRelative(1.88f, 0f, 3.19f, 1.31f)
                        reflectiveQuadTo(16.5f, 11.5f)
                        quadToRelative(0f, 0.5f, -0.1f, 0.94f)
                        reflectiveQuadTo(16.1f, 13.3f)
                        close()
                        moveToRelative(3.2f, 3.15f)
                        lineToRelative(-1.45f, -1.4f)
                        quadToRelative(0.95f, -0.72f, 1.69f, -1.59f)
                        reflectiveQuadTo(20.8f, 11.5f)
                        quadTo(19.55f, 8.98f, 17.21f, 7.49f)
                        reflectiveQuadTo(12f, 6f)
                        quadTo(11.28f, 6f, 10.58f, 6.1f)
                        reflectiveQuadTo(9.2f, 6.4f)
                        lineTo(7.65f, 4.85f)
                        quadTo(8.68f, 4.42f, 9.75f, 4.21f)
                        reflectiveQuadTo(12f, 4f)
                        quadToRelative(3.78f, 0f, 6.73f, 2.09f)
                        reflectiveQuadTo(23f, 11.5f)
                        quadToRelative(-0.57f, 1.47f, -1.51f, 2.74f)
                        reflectiveQuadTo(19.3f, 16.45f)
                        close()
                        moveToRelative(0.5f, 6.15f)
                        lineTo(15.6f, 18.45f)
                        quadToRelative(-0.88f, 0.28f, -1.76f, 0.41f)
                        reflectiveQuadTo(12f, 19f)
                        quadTo(8.23f, 19f, 5.28f, 16.91f)
                        reflectiveQuadTo(1f, 11.5f)
                        quadTo(1.53f, 10.17f, 2.33f, 9.04f)
                        reflectiveQuadTo(4.15f, 7f)
                        lineTo(1.4f, 4.2f)
                        lineTo(2.8f, 2.8f)
                        lineTo(21.2f, 21.2f)
                        lineToRelative(-1.4f, 1.4f)
                        close()
                        moveTo(5.55f, 8.4f)
                        quadTo(4.83f, 9.05f, 4.23f, 9.82f)
                        reflectiveQuadTo(3.2f, 11.5f)
                        quadToRelative(1.25f, 2.52f, 3.59f, 4.01f)
                        reflectiveQuadTo(12f, 17f)
                        quadToRelative(0.5f, 0f, 0.98f, -0.06f)
                        reflectiveQuadTo(13.95f, 16.8f)
                        lineToRelative(-0.9f, -0.95f)
                        quadToRelative(-0.28f, 0.07f, -0.53f, 0.11f)
                        reflectiveQuadTo(12f, 16f)
                        quadTo(10.13f, 16f, 8.81f, 14.69f)
                        reflectiveQuadTo(7.5f, 11.5f)
                        quadToRelative(0f, -0.28f, 0.04f, -0.53f)
                        reflectiveQuadTo(7.65f, 10.45f)
                        lineTo(5.55f, 8.4f)
                        close()
                        moveToRelative(7.98f, 2.33f)
                        close()
                        moveTo(9.75f, 12.6f)
                        close()
                    }
                }
                .build()
        return _visibilityOff!!
    }

private var _visibilityOff: ImageVector? = null


@Suppress("CheckReturnValue")
val arrowBack: ImageVector
    get() {
        if (_arrowBack != null) {
            return _arrowBack!!
        }
        _arrowBack =
            ImageVector.Builder(
                name = "arrowBack",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(7.83f, 13f)
                        lineToRelative(5.6f, 5.6f)
                        lineTo(12f, 20f)
                        lineTo(4f, 12f)
                        lineTo(12f, 4f)
                        lineToRelative(1.43f, 1.4f)
                        lineTo(7.83f, 11f)
                        horizontalLineTo(20f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(7.83f)
                        close()
                    }
                }
                .build()
        return _arrowBack!!
    }

private var _arrowBack: ImageVector? = null


// ===== Bottom Navigation Icons (importados desde SVG) =====

@Suppress("CheckReturnValue")
val homeIcon: ImageVector
    get() {
        if (_homeIcon != null) return _homeIcon!!
        _homeIcon =
            ImageVector.Builder(
                name = "homeIcon",
                defaultWidth = 19.dp,
                defaultHeight = 19.dp,
                viewportWidth = 19f,
                viewportHeight = 19f,
            ).apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.7f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(0.850098f, 7.26628f)
                    lineTo(9.1001f, 0.849609f)
                    lineTo(17.3501f, 7.26628f)
                    moveTo(2.68343f, 6.34961f)
                    verticalLineTo(17.3496f)
                    horizontalLineTo(15.5168f)
                    verticalLineTo(6.34961f)
                    moveTo(6.3501f, 17.3496f)
                    verticalLineTo(10.9329f)
                    horizontalLineTo(11.8501f)
                    verticalLineTo(17.3496f)
                }
            }.build()
        return _homeIcon!!
    }

private var _homeIcon: ImageVector? = null


@Suppress("CheckReturnValue")
val calendarIcon: ImageVector
    get() {
        if (_calendarIcon != null) return _calendarIcon!!
        _calendarIcon =
            ImageVector.Builder(
                name = "calendarIcon",
                defaultWidth = 17.dp,
                defaultHeight = 19.dp,
                viewportWidth = 17f,
                viewportHeight = 19f,
            ).apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.7f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(4.51676f, 0.849609f)
                    verticalLineTo(4.51628f)
                    moveTo(11.8501f, 0.849609f)
                    verticalLineTo(4.51628f)
                    moveTo(0.850098f, 7.26628f)
                    horizontalLineTo(15.5168f)
                    moveTo(4.51676f, 10.9329f)
                    horizontalLineTo(6.3501f)
                    moveTo(10.0168f, 10.9329f)
                    horizontalLineTo(11.8501f)
                    moveTo(4.51676f, 14.5996f)
                    horizontalLineTo(6.3501f)
                    moveTo(0.850098f, 2.68294f)
                    horizontalLineTo(15.5168f)
                    verticalLineTo(17.3496f)
                    horizontalLineTo(0.850098f)
                    verticalLineTo(2.68294f)
                    close()
                }
            }.build()
        return _calendarIcon!!
    }

private var _calendarIcon: ImageVector? = null


@Suppress("CheckReturnValue")
val appointmentIcon: ImageVector
    get() {
        if (_appointmentIcon != null) return _appointmentIcon!!
        _appointmentIcon =
            ImageVector.Builder(
                name = "appointmentIcon",
                defaultWidth = 22.dp,
                defaultHeight = 22.dp,
                viewportWidth = 22f,
                viewportHeight = 22f,
            ).apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.7f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(17.4167f, 17.4167f)
                    horizontalLineTo(19.25f)
                    verticalLineTo(19.25f)
                    horizontalLineTo(17.4167f)
                    moveTo(17.4167f, 12.8333f)
                    verticalLineTo(15.5833f)
                    moveTo(12.8333f, 18.3333f)
                    horizontalLineTo(15.5833f)
                    moveTo(2.75f, 2.75f)
                    horizontalLineTo(8.25f)
                    verticalLineTo(8.25f)
                    horizontalLineTo(2.75f)
                    verticalLineTo(2.75f)
                    close()
                    moveTo(13.75f, 2.75f)
                    horizontalLineTo(19.25f)
                    verticalLineTo(8.25f)
                    horizontalLineTo(13.75f)
                    verticalLineTo(2.75f)
                    close()
                    moveTo(2.75f, 13.75f)
                    horizontalLineTo(8.25f)
                    verticalLineTo(19.25f)
                    horizontalLineTo(2.75f)
                    verticalLineTo(13.75f)
                    close()
                    moveTo(13.75f, 13.75f)
                    horizontalLineTo(15.5833f)
                    verticalLineTo(15.5833f)
                    horizontalLineTo(13.75f)
                    verticalLineTo(13.75f)
                    close()
                }
            }.build()
        return _appointmentIcon!!
    }

private var _appointmentIcon: ImageVector? = null


@Suppress("CheckReturnValue")
val sunIcon: ImageVector
    get() {
        if (_sunIcon != null) return _sunIcon!!
        _sunIcon =
            ImageVector.Builder(
                name = "sunIcon",
                defaultWidth = 22.dp,
                defaultHeight = 22.dp,
                viewportWidth = 22f,
                viewportHeight = 22f,
            ).apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 1.7f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(10.9999f, 1.83398f)
                    verticalLineTo(4.58398f)
                    moveTo(10.9999f, 17.4173f)
                    verticalLineTo(20.1673f)
                    moveTo(1.83325f, 11.0007f)
                    horizontalLineTo(4.58325f)
                    moveTo(17.4166f, 11.0007f)
                    horizontalLineTo(20.1666f)
                    moveTo(4.58325f, 4.58398f)
                    lineTo(6.41659f, 6.41732f)
                    moveTo(15.5833f, 15.584f)
                    lineTo(17.4166f, 17.4173f)
                    moveTo(4.58325f, 17.4173f)
                    lineTo(6.41659f, 15.584f)
                    moveTo(15.5833f, 6.41732f)
                    lineTo(17.4166f, 4.58398f)
                    moveTo(10.9999f, 7.33398f)
                    curveTo(10.0275f, 7.33398f, 9.09483f, 7.72029f, 8.40719f, 8.40793f)
                    curveTo(7.71956f, 9.09556f, 7.33325f, 10.0282f, 7.33325f, 11.0007f)
                    curveTo(7.33325f, 11.9731f, 7.71956f, 12.9057f, 8.40719f, 13.5934f)
                    curveTo(9.09483f, 14.281f, 10.0275f, 14.6673f, 10.9999f, 14.6673f)
                    curveTo(11.9724f, 14.6673f, 12.905f, 14.281f, 13.5926f, 13.5934f)
                    curveTo(14.2803f, 12.9057f, 14.6666f, 11.9731f, 14.6666f, 11.0007f)
                    curveTo(14.6666f, 10.0282f, 14.2803f, 9.09556f, 13.5926f, 8.40793f)
                    curveTo(12.905f, 7.72029f, 11.9724f, 7.33398f, 10.9999f, 7.33398f)
                    close()
                }
            }.build()
        return _sunIcon!!
    }

private var _sunIcon: ImageVector? = null


// ===== Add/Plus Icon (Parientes vinculados vacío) =====

@Suppress("CheckReturnValue")
val addCircleOutlineIcon: ImageVector
    get() {
        if (_addCircleOutlineIcon != null) return _addCircleOutlineIcon!!
        _addCircleOutlineIcon =
            ImageVector.Builder(
                name = "addCircleOutlineIcon",
                defaultWidth = 48.dp,
                defaultHeight = 48.dp,
                viewportWidth = 48f,
                viewportHeight = 48f,
            ).apply {
                path(
                    fill = null,
                    fillAlpha = 1f,
                    stroke = SolidColor(Color.Black),
                    strokeAlpha = 1f,
                    strokeLineWidth = 2.5f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round,
                    strokeLineMiter = 1f,
                    pathFillType = PathFillType.NonZero,
                ) {
                    // Cruz (plus)
                    moveTo(24f, 12f)
                    verticalLineTo(36f)
                    moveTo(12f, 24f)
                    horizontalLineTo(36f)
                }
            }.build()
        return _addCircleOutlineIcon!!
    }

private var _addCircleOutlineIcon: ImageVector? = null