package pe.edu.upc.saludya.iam

import org.junit.Assert.*
import org.junit.Test
import pe.edu.upc.saludya.iam.domain.model.*

class CredentialsValidatorTest {
    private val valid = CredentialsForm("lucia@example.com", "SaludYa123", "SaludYa123", "987654321")

    @Test fun `valid credentials normalize email but retain password exactly`() {
        val form = valid.copy(email = " LucIA@Example.com ", password = " SaludYa123 ", confirmPassword = " SaludYa123 ")
        val result = CredentialsValidator.validate(form)
        assertTrue(result.errors.isEmpty())
        assertEquals("lucia@example.com", result.credentials?.email)
        assertEquals(form.password, result.credentials?.password)
    }

    @Test fun `different passwords reject the confirmation`() {
        val result = CredentialsValidator.validate(valid.copy(confirmPassword = "SaludYa124"))
        assertNull(result.credentials)
        assertEquals(CredentialsError.PASSWORD_MISMATCH, result.errors[CredentialsField.CONFIRM_PASSWORD])
    }

    @Test fun `invalid email identifies the email field`() {
        for (email in listOf("", "lucia", "lucia@", "@example.com", "a b@example.com", "a@@example.com", "a@localhost")) {
            assertEquals(CredentialsError.EMAIL_FORMAT,
                CredentialsValidator.validate(valid.copy(email = email)).errors[CredentialsField.EMAIL])
        }
    }

    @Test fun `phone must be nine ASCII digits without silently modifying input`() {
        for (phone in listOf("", "12345678", "1234567890", "98765432a", "+51987654321", "987 65432", "９８７６５４３２１")) {
            assertEquals(CredentialsError.PHONE_FORMAT,
                CredentialsValidator.validate(valid.copy(phone = phone)).errors[CredentialsField.PHONE])
        }
    }

    @Test fun `all invalid fields are returned together`() {
        val result = CredentialsValidator.validate(CredentialsForm("invalid", "short", "other", "1"))
        assertEquals(CredentialsField.entries.toSet(), result.errors.keys)
        assertNull(result.credentials)
    }

    @Test fun `password policy respects UTF8 BCrypt limit and requires letters and digits`() {
        for (password in listOf("short1", "abcdefgh", "12345678", "a1" + "ñ".repeat(36))) {
            assertEquals(CredentialsError.PASSWORD_POLICY,
                CredentialsValidator.validate(valid.copy(password = password, confirmPassword = password)).errors[CredentialsField.PASSWORD])
        }
    }

    @Test fun `credentials and form do not expose passwords in string representations`() {
        assertFalse(valid.toString().contains("SaludYa123"))
        assertFalse(CredentialsValidator.validate(valid).credentials.toString().contains("SaludYa123"))
    }
}
