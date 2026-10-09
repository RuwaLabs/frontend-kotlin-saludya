package pe.edu.upc.saludya.features.configuration.presentation.validation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.configuration.presentation.components.PrimaryButton

/**
 * Modal genérico para mostrar un mensaje de validación/error/éxito al usuario.
 */
@Composable
fun ValidationDialog(
    message: String,
    onCorrectClick: () -> Unit,
    @StringRes titleRes: Int = R.string.validation_default_title,
    @StringRes buttonTextRes: Int = R.string.validation_default_button,
) {
    Dialog(
        onDismissRequest = { /* No dismissable: el usuario debe confirmar */ },
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
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                Text(
                    text = stringResource(titleRes),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp,
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = stringResource(buttonTextRes),
                    onClick = onCorrectClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "ValidationDialog - Error")
@Composable
private fun ValidationDialogErrorPreview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            ValidationDialog(
                titleRes = R.string.error_wrong_code_title,
                message = "Revisa los 6 dígitos e inténtalo otra vez.",
                buttonTextRes = R.string.dialog_understood,
                onCorrectClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "ValidationDialog - Éxito")
@Composable
private fun ValidationDialogSuccessPreview() {
    SaludYaTheme(dynamicColor = false) {
        Surface(color = Color(0x80000000)) {
            ValidationDialog(
                titleRes = R.string.success_change_done_title,
                message = "Verificamos el código y actualizamos tu contacto.",
                buttonTextRes = R.string.dialog_understood,
                onCorrectClick = {},
            )
        }
    }
}