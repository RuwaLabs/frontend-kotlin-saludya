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
 * H10: Modal de éxito tras guardar una regla.
 *
 * Se muestra después de que el usuario guarda un cambio en H03 (Editar regla)
 * o H04 (Edición rápida). Su único botón lleva de vuelta a H01.
 */
@Composable
fun RuleUpdatedDialog(
    message: String,
    onBackToConfigurationClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { /* No dismissable - el usuario debe confirmar */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
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
                    text = "Regla actualizada",
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
                    text = "Volver a configuración",
                    onClick = onBackToConfigurationClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "RuleUpdatedDialog")
@Composable
private fun RuleUpdatedDialogPreview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            RuleUpdatedDialog(
                message = "Se guardó la nueva capacidad. Las reservas confirmadas se conservan.",
                onBackToConfigurationClick = {},
            )
        }
    }
}