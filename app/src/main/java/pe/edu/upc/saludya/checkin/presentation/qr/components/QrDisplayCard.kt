package pe.edu.upc.saludya.checkin.presentation.qr.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.edu.upc.saludya.checkin.domain.entity.QrCodeInfo
import kotlin.random.Random

/** Número de módulos por lado (equivale a un QR versión 1). */
private const val QR_MODULES = 21

/**
 * Tarjeta blanca de bordes muy redondeados que muestra el código QR y su leyenda.
 *
 * @param qrCodeInfo datos del QR; si es `null` se muestra un mensaje vacío.
 * @param isLoading muestra un indicador de progreso en lugar del QR.
 */
@Composable
fun QrDisplayCard(
    qrCodeInfo: QrCodeInfo?,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.onSurfaceVariant.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    isLoading -> CircularProgressIndicator(color = colors.primary)
                    qrCodeInfo != null -> SampleQrCode(
                        code = qrCodeInfo.token,
                        color = colors.onSurface,
                        modifier = Modifier.size(200.dp),
                    )
                    else -> Text(
                        text = "No hay un código disponible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
            if (qrCodeInfo != null && !isLoading) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = buildString {
                        if (qrCodeInfo.isSample) append("QR de muestra · ")
                        append(qrCodeInfo.token)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Dibuja un QR **de muestra** (no escaneable) derivado de forma determinista de [code].
 *
 * Para un QR real, sustituir [buildSampleMatrix] por la matriz generada con una
 * librería como ZXing (`QRCodeWriter().encode(...)`) y reutilizar este mismo Canvas.
 */
@Composable
private fun SampleQrCode(
    code: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val matrix = remember(code) { buildSampleMatrix(code) }

    Canvas(
        modifier = modifier.semantics { contentDescription = "Código QR $code" },
    ) {
        val cell = size.minDimension / QR_MODULES
        for (row in 0 until QR_MODULES) {
            for (col in 0 until QR_MODULES) {
                if (matrix[row][col]) {
                    drawRect(
                        color = color,
                        topLeft = Offset(col * cell, row * cell),
                        // +0.5px evita líneas finas entre módulos por redondeo.
                        size = Size(cell + 0.5f, cell + 0.5f),
                    )
                }
            }
        }
    }
}

/** Genera la matriz de módulos: tres patrones de posición + relleno pseudoaleatorio estable. */
private fun buildSampleMatrix(code: String): Array<BooleanArray> {
    val random = Random(code.hashCode())
    val matrix = Array(QR_MODULES) { BooleanArray(QR_MODULES) }
    val finderOrigins = listOf(0 to 0, 0 to QR_MODULES - 7, QR_MODULES - 7 to 0)

    fun finderOriginFor(row: Int, col: Int): Pair<Int, Int>? =
        finderOrigins.firstOrNull { (r, c) -> row in (r - 1)..(r + 7) && col in (c - 1)..(c + 7) }

    for (row in 0 until QR_MODULES) {
        for (col in 0 until QR_MODULES) {
            val origin = finderOriginFor(row, col)
            matrix[row][col] = if (origin == null) {
                random.nextBoolean()
            } else {
                val r = row - origin.first
                val c = col - origin.second
                val insideFinder = r in 0..6 && c in 0..6
                val outerRing = r == 0 || r == 6 || c == 0 || c == 6
                val center = r in 2..4 && c in 2..4
                insideFinder && (outerRing || center)
            }
        }
    }
    return matrix
}
