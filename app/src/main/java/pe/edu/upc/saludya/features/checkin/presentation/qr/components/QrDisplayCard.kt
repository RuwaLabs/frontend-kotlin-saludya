package pe.edu.upc.saludya.features.checkin.presentation.qr.components

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
import androidx.compose.ui.res.stringResource
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

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
                    qrCodeInfo != null -> QrCodeImage(
                        code = qrCodeInfo.token,
                        color = colors.onSurface,
                        modifier = Modifier.size(200.dp),
                    )
                    else -> Text(
                        text = stringResource(R.string.checkin_qr_no_code),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant,
                    )
                }
            }
            if (qrCodeInfo != null && !isLoading) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.checkin_qr_description),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * Genera y dibuja un código QR real a partir del token firmado por el backend.
 */
@Composable
private fun QrCodeImage(
    code: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val qrDescription = stringResource(R.string.checkin_qr_content_description)

    val matrix = remember(code) {
        QRCodeWriter().encode(
            code,
            BarcodeFormat.QR_CODE,
            512,
            512,
            mapOf(
                EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.M,
                EncodeHintType.MARGIN to 2,
            ),
        )
    }

    Canvas(
        modifier = modifier.semantics {
            contentDescription = qrDescription
        },
    ) {
        val cellWidth = size.width / matrix.width
        val cellHeight = size.height / matrix.height

        for (row in 0 until matrix.height) {
            for (col in 0 until matrix.width) {
                if (matrix[col, row]) {
                    drawRect(
                        color = color,
                        topLeft = Offset(
                            x = col * cellWidth,
                            y = row * cellHeight,
                        ),
                        size = Size(
                            width = cellWidth + 0.5f,
                            height = cellHeight + 0.5f,
                        ),
                    )
                }
            }
        }
    }
}