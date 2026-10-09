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

@Suppress("CheckReturnValue")
public val health_cross: ImageVector
    get() {
        if (_health_cross != null) {
            return _health_cross!!
        }
        _health_cross =
            ImageVector.Builder(
                name = "health_cross",
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
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(8f, 21f)
                        verticalLineTo(16f)
                        horizontalLineTo(3f)
                        verticalLineTo(8f)
                        horizontalLineTo(8f)
                        verticalLineTo(3f)
                        horizontalLineToRelative(8f)
                        verticalLineTo(8f)
                        horizontalLineToRelative(5f)
                        verticalLineToRelative(8f)
                        horizontalLineTo(16f)
                        verticalLineToRelative(5f)
                        horizontalLineTo(8f)
                        close()
                        moveToRelative(2f, -2f)
                        horizontalLineToRelative(4f)
                        verticalLineTo(14f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(10f)
                        horizontalLineTo(14f)
                        verticalLineTo(5f)
                        horizontalLineTo(10f)
                        verticalLineToRelative(5f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(4f)
                        horizontalLineToRelative(5f)
                        verticalLineToRelative(5f)
                        close()
                        moveToRelative(2f, -7f)
                        close()
                    }
                }
                .build()
        return _health_cross!!
    }

private var _health_cross: ImageVector? = null

// ==== PLACEHOLDERS TEMPORALES (reemplazar por los SVG reales) ====
@Suppress("CheckReturnValue")
val qrCodeIcon: ImageVector
    get() {
        if (_qrCodeIcon != null) return _qrCodeIcon!!
        _qrCodeIcon =
            ImageVector.Builder(
                name = "qrCodeIcon",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(3f, 3f); horizontalLineToRelative(8f); verticalLineToRelative(8f)
                    horizontalLineToRelative(-8f); close()
                    moveTo(13f, 3f); horizontalLineToRelative(8f); verticalLineToRelative(8f)
                    horizontalLineToRelative(-8f); close()
                    moveTo(3f, 13f); horizontalLineToRelative(8f); verticalLineToRelative(8f)
                    horizontalLineToRelative(-8f); close()
                    moveTo(13f, 13f); horizontalLineToRelative(3f); verticalLineToRelative(3f)
                    horizontalLineToRelative(-3f); close()
                    moveTo(19f, 19f); horizontalLineToRelative(2f); verticalLineToRelative(2f)
                    horizontalLineToRelative(-2f); close()
                }
            }.build()
        return _qrCodeIcon!!
    }

private var _qrCodeIcon: ImageVector? = null


@Suppress("CheckReturnValue")
val settingsIcon: ImageVector
    get() {
        if (_settingsIcon != null) return _settingsIcon!!
        _settingsIcon =
            ImageVector.Builder(
                name = "settingsIcon",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            ).apply {
                path(
                    fill = SolidColor(Color.Black),
                    stroke = null,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(12f, 8f)
                    curveTo(9.79f, 8f, 8f, 9.79f, 8f, 12f)
                    curveTo(8f, 14.21f, 9.79f, 16f, 12f, 16f)
                    curveTo(14.21f, 16f, 16f, 14.21f, 16f, 12f)
                    curveTo(16f, 9.79f, 14.21f, 8f, 12f, 8f)
                    close()
                    moveTo(19.43f, 12.98f)
                    curveTo(19.47f, 12.66f, 19.5f, 12.34f, 19.5f, 12f)
                    curveTo(19.5f, 11.66f, 19.47f, 11.34f, 19.43f, 11.02f)
                    lineTo(21.54f, 9.37f)
                    lineTo(19.54f, 5.91f)
                    lineTo(17.03f, 6.92f)
                    curveTo(16.5f, 6.53f, 15.93f, 6.2f, 15.3f, 5.95f)
                    lineTo(14.94f, 3.26f)
                    lineTo(11.06f, 3.26f)
                    lineTo(10.7f, 5.95f)
                    curveTo(10.07f, 6.2f, 9.5f, 6.53f, 8.97f, 6.92f)
                    lineTo(6.46f, 5.91f)
                    lineTo(4.46f, 9.37f)
                    lineTo(6.57f, 11.02f)
                    curveTo(6.53f, 11.34f, 6.5f, 11.67f, 6.5f, 12f)
                    curveTo(6.5f, 12.33f, 6.53f, 12.66f, 6.57f, 12.98f)
                    lineTo(4.46f, 14.63f)
                    lineTo(6.46f, 18.09f)
                    lineTo(8.97f, 17.08f)
                    curveTo(9.5f, 17.47f, 10.07f, 17.8f, 10.7f, 18.05f)
                    lineTo(11.06f, 20.74f)
                    lineTo(14.94f, 20.74f)
                    lineTo(15.3f, 18.05f)
                    curveTo(15.93f, 17.8f, 16.5f, 17.47f, 17.03f, 17.08f)
                    lineTo(19.54f, 18.09f)
                    lineTo(21.54f, 14.63f)
                    close()
                }
            }.build()
        return _settingsIcon!!
    }

private var _settingsIcon: ImageVector? = null

@Suppress("CheckReturnValue")
public val qr_code: ImageVector
    get() {
        if (_qr_code != null) {
            return _qr_code!!
        }
        _qr_code =
            ImageVector.Builder(
                name = "qr_code",
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
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(3f, 11f)
                        verticalLineTo(3f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(8f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(5f, 9f)
                        horizontalLineTo(9f)
                        verticalLineTo(5f)
                        horizontalLineTo(5f)
                        verticalLineTo(9f)
                        close()
                        moveTo(3f, 21f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(8f)
                        horizontalLineTo(3f)
                        close()
                        moveTo(5f, 19f)
                        horizontalLineTo(9f)
                        verticalLineTo(15f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(4f)
                        close()
                        moveToRelative(8f, -8f)
                        verticalLineTo(3f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(8f)
                        horizontalLineTo(13f)
                        close()
                        moveTo(15f, 9f)
                        horizontalLineToRelative(4f)
                        verticalLineTo(5f)
                        horizontalLineTo(15f)
                        verticalLineTo(9f)
                        close()
                        moveToRelative(4f, 12f)
                        verticalLineTo(19f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(19f)
                        close()
                        moveTo(13f, 15f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(13f)
                        close()
                        moveToRelative(2f, 2f)
                        verticalLineTo(15f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(15f)
                        close()
                        moveToRelative(-2f, 2f)
                        verticalLineTo(17f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(13f)
                        close()
                        moveToRelative(2f, 2f)
                        verticalLineTo(19f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(15f)
                        close()
                        moveToRelative(2f, -2f)
                        verticalLineTo(17f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(17f)
                        close()
                        moveToRelative(0f, -4f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(17f)
                        close()
                        moveToRelative(2f, 2f)
                        verticalLineTo(15f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(19f)
                        close()
                    }
                }
                .build()
        return _qr_code!!
    }

private var _qr_code: ImageVector? = null

@Suppress("CheckReturnValue")
public val settings: ImageVector
    get() {
        if (_settings != null) {
            return _settings!!
        }
        _settings =
            ImageVector.Builder(
                name = "settings",
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
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(9.25f, 22f)
                        lineTo(8.85f, 18.8f)
                        quadTo(8.53f, 18.68f, 8.24f, 18.5f)
                        reflectiveQuadTo(7.68f, 18.13f)
                        lineTo(4.7f, 19.38f)
                        lineTo(1.95f, 14.63f)
                        lineTo(4.53f, 12.68f)
                        quadTo(4.5f, 12.5f, 4.5f, 12.34f)
                        quadToRelative(0f, -0.16f, 0f, -0.34f)
                        reflectiveQuadToRelative(0f, -0.34f)
                        reflectiveQuadTo(4.53f, 11.33f)
                        lineTo(1.95f, 9.38f)
                        lineTo(4.7f, 4.63f)
                        lineTo(7.68f, 5.88f)
                        quadTo(7.95f, 5.68f, 8.25f, 5.5f)
                        reflectiveQuadTo(8.85f, 5.2f)
                        lineTo(9.25f, 2f)
                        horizontalLineToRelative(5.5f)
                        lineToRelative(0.4f, 3.2f)
                        quadToRelative(0.33f, 0.13f, 0.61f, 0.3f)
                        reflectiveQuadToRelative(0.56f, 0.38f)
                        lineTo(19.3f, 4.63f)
                        lineToRelative(2.75f, 4.75f)
                        lineToRelative(-2.57f, 1.95f)
                        quadToRelative(0.02f, 0.18f, 0.02f, 0.34f)
                        reflectiveQuadToRelative(0f, 0.34f)
                        reflectiveQuadToRelative(0f, 0.34f)
                        reflectiveQuadToRelative(-0.05f, 0.34f)
                        lineToRelative(2.57f, 1.95f)
                        lineToRelative(-2.75f, 4.75f)
                        lineTo(16.33f, 18.13f)
                        quadToRelative(-0.27f, 0.2f, -0.57f, 0.38f)
                        reflectiveQuadToRelative(-0.6f, 0.3f)
                        lineTo(14.75f, 22f)
                        horizontalLineTo(9.25f)
                        close()
                        moveTo(11f, 20f)
                        horizontalLineToRelative(1.98f)
                        lineToRelative(0.35f, -2.65f)
                        quadToRelative(0.78f, -0.2f, 1.44f, -0.59f)
                        reflectiveQuadToRelative(1.21f, -0.94f)
                        lineToRelative(2.47f, 1.03f)
                        lineToRelative(0.98f, -1.7f)
                        lineTo(17.28f, 13.52f)
                        quadToRelative(0.13f, -0.35f, 0.17f, -0.74f)
                        reflectiveQuadTo(17.5f, 12f)
                        reflectiveQuadTo(17.45f, 11.21f)
                        quadTo(17.4f, 10.83f, 17.28f, 10.48f)
                        lineTo(19.43f, 8.85f)
                        lineTo(18.45f, 7.15f)
                        lineTo(15.98f, 8.2f)
                        quadTo(15.43f, 7.63f, 14.76f, 7.24f)
                        reflectiveQuadTo(13.33f, 6.65f)
                        lineTo(13f, 4f)
                        horizontalLineTo(11.03f)
                        lineTo(10.68f, 6.65f)
                        quadTo(9.9f, 6.85f, 9.24f, 7.24f)
                        reflectiveQuadTo(8.03f, 8.17f)
                        lineTo(5.55f, 7.15f)
                        lineTo(4.58f, 8.85f)
                        lineToRelative(2.15f, 1.6f)
                        quadTo(6.6f, 10.83f, 6.55f, 11.2f)
                        reflectiveQuadTo(6.5f, 12f)
                        quadToRelative(0f, 0.4f, 0.05f, 0.77f)
                        reflectiveQuadToRelative(0.17f, 0.75f)
                        lineTo(4.58f, 15.15f)
                        lineToRelative(0.98f, 1.7f)
                        lineTo(8.03f, 15.8f)
                        quadToRelative(0.55f, 0.58f, 1.21f, 0.96f)
                        reflectiveQuadToRelative(1.44f, 0.59f)
                        lineTo(11f, 20f)
                        close()
                        moveToRelative(1.05f, -4.5f)
                        quadToRelative(1.45f, 0f, 2.47f, -1.03f)
                        reflectiveQuadTo(15.55f, 12f)
                        reflectiveQuadTo(14.53f, 9.52f)
                        reflectiveQuadTo(12.05f, 8.5f)
                        quadToRelative(-1.47f, 0f, -2.49f, 1.02f)
                        reflectiveQuadTo(8.55f, 12f)
                        reflectiveQuadToRelative(1.01f, 2.47f)
                        reflectiveQuadToRelative(2.49f, 1.03f)
                        close()
                        moveTo(12f, 12f)
                        close()
                    }
                }
                .build()
        return _settings!!
    }

private var _settings: ImageVector? = null
