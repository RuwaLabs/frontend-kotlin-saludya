package pe.edu.upc.saludya.features.configuration.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme

private val DescriptionColor = Color(0xFF56706D)

@Composable
fun ScreenDescription(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal,
        color = DescriptionColor,
        lineHeight = 22.sp,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, name = "ScreenDescription")
@Composable
private fun ScreenDescriptionPreview() {
    SaludYaTheme(dynamicColor = false) {
        ScreenDescription(
            text = "Confirmarás el cambio con un código enviado a tu correo registrado. El celular es un dato de contacto.",
        )
    }
}