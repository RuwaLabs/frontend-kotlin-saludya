package pe.edu.upc.saludya.features.configuration.presentation.validation

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
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton

private val TitleColor = Color(0xFF181C1C)
private val MessageColor = Color(0xFF56706D)

/**
 * Modal genérico para mostrar un mensaje de validación/error al usuario.
 *
 * Reutilizable en:
 *  - H05 "Revisa el valor" (validación de coherencia de regla)
 *  - H12 "Revisa las fechas" (rango de fechas inválido)
 */
@Composable
fun ValidationDialog(
    message: String,
    onCorrectClick: () -> Unit,
    title: String = "Revisa el valor",
    buttonText: String = "Corregir valor",
) {
    Dialog(
        onDismissRequest = { /* No dismissable: el usuario debe corregir */ },
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
                    text = buttonText,
                    onClick = onCorrectClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "ValidationDialog - H05")
@Composable
private fun ValidationDialogH05Preview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            ValidationDialog(
                message = "La capacidad debe ser mayor que cero. La tolerancia de llegada no puede superar la duración del intervalo. Revisa los campos antes de guardar.",
                onCorrectClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "ValidationDialog - H12")
@Composable
private fun ValidationDialogH12Preview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            ValidationDialog(
                title = "Revisa las fechas",
                message = "La fecha inicial no puede ser posterior a la fecha final.",
                buttonText = "Corregir fechas",
                onCorrectClick = {},
            )
        }
    }
}