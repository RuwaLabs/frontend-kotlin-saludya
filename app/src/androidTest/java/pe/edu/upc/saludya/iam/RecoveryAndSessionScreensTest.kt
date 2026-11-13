package pe.edu.upc.saludya.features.iam

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.iam.domain.repository.AccessFailure
import pe.edu.upc.saludya.features.iam.presentation.access.*

class RecoveryAndSessionScreensTest {
    @get:Rule val compose = createComposeRule()
    @Test fun resetMismatchIdentifiesConfirmationField() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            PasswordResetScreen(PasswordResetUiState(validLink = true, mismatch = true), {}, {}, {}, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Las contraseñas no coinciden.").performScrollTo().assertIsDisplayed()
    }
    @Test fun expiredResetLinkRequiresANewRequest() {
        var requests = 0
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            PasswordResetScreen(PasswordResetUiState(failure = AccessFailure.RESET_LINK_INVALID), {}, {}, {}, {}, {}, { requests++ }, {})
        } }
        compose.onNodeWithText("El enlace expiró o ya no es válido. Solicita un nuevo enlace de recuperación.")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Guardar contraseña").assertDoesNotExist()
        compose.onNodeWithText("Solicitar nuevo enlace").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, requests) }
    }
    @Test fun successfulResetOffersSignIn() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            PasswordResetScreen(PasswordResetUiState(updated = true), {}, {}, {}, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Tu contraseña se actualizó. Ya puedes iniciar sesión.").assertIsDisplayed()
        compose.onNodeWithText("Iniciar sesión").assertIsEnabled()
    }
    @Test fun helpShowsBothInvalidFields() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RecoveryHelpScreen(RecoveryHelpUiState(invalidDni = true, invalidEmail = true), {}, {}, {}, {})
        } }
        compose.onNodeWithText("El DNI debe tener exactamente 8 dígitos numéricos.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ingresa un correo electrónico válido.").performScrollTo().assertIsDisplayed()
    }
    @Test fun logoutDialogCanBeCancelledWithoutCallingConfirmation() {
        var cancelled = 0; var confirmed = 0
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            PatientSessionScreen(LoginUiState(logoutConfirmation = true), {}, { cancelled++ }, { confirmed++ }, {})
        } }
        compose.onNodeWithText("¿Cerrar sesión?").assertIsDisplayed()
        compose.onNodeWithText("Cancelar").performClick()
        compose.runOnIdle { assertEquals(1, cancelled); assertEquals(0, confirmed) }
    }
    @Test fun expiredSessionRequiresSignInAndCannotShowProtectedAction() {
        var logins = 0
        compose.setContent { SaludYaTheme(dynamicColor = false) { SessionExpiredScreen { logins++ } } }
        compose.onNodeWithText("Tu sesión expiró").assertIsDisplayed()
        compose.onNodeWithText("Ver mi perfil").assertDoesNotExist()
        compose.onNodeWithText("Iniciar sesión").performClick()
        compose.runOnIdle { assertEquals(1, logins) }
    }
}
