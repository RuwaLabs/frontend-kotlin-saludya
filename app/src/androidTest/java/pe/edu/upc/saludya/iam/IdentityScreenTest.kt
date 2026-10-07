package pe.edu.upc.saludya.iam

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.presentation.identity.*
import java.time.LocalDate

class IdentityScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun continuationIsDisabledUntilBackendVerification() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            IdentityScreen(IdentityUiState(), { _, _ -> }, {}, {})
        } }
        compose.onNodeWithText("Continuar registro").performScrollTo().assertIsNotEnabled()
    }

    @Test fun verifiedIdentityEnablesContinuation() {
        val details = IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1))
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            IdentityScreen(IdentityUiState(status = IdentityStatus.VERIFIED, verifiedIdentity = VerifiedIdentity(details)), { _, _ -> }, {}, {})
        } }
        compose.onNodeWithText("Identidad verificada").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Continuar registro").performScrollTo().assertIsEnabled()
    }

    @Test fun providerOutageShowsRetryAndBlocksContinuation() {
        compose.setContent { SaludYaTheme(dynamicColor = false) {
            IdentityScreen(IdentityUiState(status = IdentityStatus.UNAVAILABLE), { _, _ -> }, {}, {})
        } }
        compose.onNodeWithText("Reintentar verificación").performScrollTo().assertIsEnabled()
        compose.onNodeWithText("Continuar registro").performScrollTo().assertIsNotEnabled()
    }
}
