package pe.edu.upc.saludya.features.iam

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.features.iam.domain.model.*
import pe.edu.upc.saludya.features.iam.domain.repository.RegistrationReceipt
import pe.edu.upc.saludya.features.iam.presentation.registration.*
import pe.edu.upc.saludya.features.iam.presentation.access.CodeVerificationScreen
import java.time.LocalDate

class RegistrationScreenTest {
    @get:Rule val compose = createComposeRule()
    private val identity = VerifiedIdentity(IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1)))

    @Test fun credentialsCannotBeSubmittedWithoutIdentity() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RegistrationScreen(RegistrationUiState(), { _, _ -> }, {}, {}, {})
        } }
        compose.onNodeWithText("Continuar", useUnmergedTree = true).performScrollTo().assertIsNotEnabled()
    }

    @Test fun passwordMismatchAndEmailConflictAreShownByTheirFields() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RegistrationScreen(RegistrationUiState(identity = identity,
                errors = mapOf(CredentialsField.EMAIL to CredentialsError.EMAIL_IN_USE,
                    CredentialsField.CONFIRM_PASSWORD to CredentialsError.PASSWORD_MISMATCH)), { _, _ -> }, {}, {}, {})
        } }
        compose.onNodeWithText("Este correo ya está en uso. Ingresa otro correo.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Las contraseñas no coinciden.").performScrollTo().assertIsDisplayed()
    }

    @Test fun createdAccountEnablesSignIn() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RegistrationScreen(RegistrationUiState(identity = identity,
                status = RegistrationStatus.ACCEPTED, receipt = RegistrationReceipt(1, 2, "lucia@example.com")), { _, _ -> }, {}, {}, {})
        } }
        compose.onNodeWithText("Tu cuenta está lista. Ya puedes iniciar sesión.").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ir a iniciar sesión").performScrollTo().assertIsEnabled()
    }

    @Test fun pendingSubmissionDisablesContinueAndBack() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            RegistrationScreen(RegistrationUiState(identity = identity, status = RegistrationStatus.SUBMITTING), { _, _ -> }, {}, {}, {})
        } }
        compose.onNodeWithText("Continuar", useUnmergedTree = true).performScrollTo().assertIsNotEnabled()
        compose.onNodeWithText("Volver").performScrollTo().assertIsNotEnabled()
    }

    @Test fun verificationShowsRecipientAndCodeActions() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            CodeVerificationScreen("lucia@example.com", "", false, null, false, {}, {}, {}, {})
        } }
        compose.onNodeWithText("Código de verificación").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ingresa el código de 6 dígitos enviado a lucia@example.com.").assertIsDisplayed()
    }
}
