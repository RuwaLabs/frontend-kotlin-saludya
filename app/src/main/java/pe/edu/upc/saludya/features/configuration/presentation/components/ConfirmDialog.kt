package pe.edu.upc.saludya.features.configuration.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

private val TitleColor = Color(0xFF181C1C)
private val MessageColor = Color(0xFF56706D)

/**
 * Modal de confirmación reutilizable.
 *
 * Tiene un botón principal (acción) y uno secundario (cancelar).
 * Ideal para acciones destructivas como "Dejar vinculación".
 */
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MessageColor,
                    lineHeight = 22.sp,
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = confirmText,
                    onClick = onConfirm,
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlineActionButton(
                    text = cancelText,
                    onClick = onCancel,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "ConfirmDialog")
@Composable
private fun ConfirmDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            ConfirmDialog(
                title = "¿Dejar esta vinculación?",
                message = "Dejarás de gestionar las citas de Mateo desde esta cuenta. Sus reservas se conservan.",
                confirmText = "Dejar vinculación",
                cancelText = "Conservar vínculo",
                onConfirm = {},
                onCancel = {},
            )
        }
    }
}