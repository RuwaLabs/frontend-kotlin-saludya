package pe.edu.upc.saludya.features.iam

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.iam.domain.repository.AccessFailure
import pe.edu.upc.saludya.features.iam.presentation.access.*

class AccessScreensTest {
    @get:Rule val compose = createComposeRule()

    @Test fun loginDoesNotOfferAUserRoleSelector() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            LoginScreen(LoginUiState(), {}, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Correo electrónico").assertIsDisplayed()
        compose.onNodeWithText("Tipo de usuario").assertDoesNotExist()
        compose.onNodeWithText("¿Olvidaste tu contraseña?").performScrollTo().assertIsEnabled()
    }

    @Test fun invalidCredentialsDoNotIdentifyWhichCredentialFailed() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            LoginScreen(LoginUiState(failure = AccessFailure.INVALID_CREDENTIALS), {}, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Credenciales inválidas. Revisa tu correo y contraseña.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Correo no registrado").assertDoesNotExist()
    }

    @Test fun inactiveAccountIndicatesAdmissionSupport() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            LoginScreen(LoginUiState(failure = AccessFailure.INACTIVE_ACCOUNT), {}, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Tu cuenta está inactiva. Consulta con el personal de admisión.").performScrollTo().assertIsDisplayed()
    }

    @Test fun incorrectCodeDisplaysRetryAndResendActions() {
        var resends = 0
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            CodeVerificationScreen("l***@example.com", "000000", false,
                "El código es incorrecto. Revísalo e intenta nuevamente.", true, {}, {}, { resends++ }, {})
        } }
        compose.onNodeWithText("El código es incorrecto. Revísalo e intenta nuevamente.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Reenviar código").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, resends) }
    }

    @Test fun pendingCodeRequestDisablesVerificationResendAndBack() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            CodeVerificationScreen("l***@example.com", "", true, null, false, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Crear mi cuenta").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("Reenviar código").performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("Volver").performScrollTo().assertIsNotEnabled()
    }

    @Test fun recoveryUsesAGenericConfirmationAndAllowsAnotherRequest() {
        var requests = 0
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RecoveryScreen(RecoveryUiState(email = "lucia@example.com", requested = true), {}, { requests++ }, {})
        } }
        compose.onNodeWithText("Si existe una cuenta activa asociada a este correo, recibirás un enlace de recuperación. Revisa también la carpeta de spam.")
            .performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Solicitar otro enlace").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, requests) }
    }
}
