package pe.edu.upc.saludya.features.iam

import org.junit.Assert.*
import org.junit.Test
import pe.edu.upc.saludya.features.iam.domain.model.PasswordResetLink

class PasswordResetLinkTest {
    private val parser = PasswordResetLink()
    private val token = "A".repeat(43)
    @Test fun `accepts an HTTPS link for the configured host and reset path`() {
        assertEquals(token, parser.token("https://saludya.test/reset-password?token=$token", "saludya.test"))
        assertEquals(token, parser.token("https://SALUDYA.test:443/reset-password?token=$token", "saludya.test"))
    }
    @Test fun `foreign insecure or misleading links cannot supply a token`() {
        for (link in listOf("http://saludya.test/reset-password?token=$token",
            "https://evil.test/reset-password?token=$token", "https://saludya.test.evil.test/reset-password?token=$token",
            "https://saludya.test@evil.test/reset-password?token=$token", "https://user@saludya.test/reset-password?token=$token",
            "https://saludya.test:444/reset-password?token=$token", "https://saludya.test/other?token=$token")) {
            assertNull(parser.token(link, "saludya.test"))
        }
    }
    @Test fun `missing malformed duplicated or fragment tokens are rejected`() {
        for (suffix in listOf("", "?token=short", "?token=$token&token=$token", "?token=$token&%74oken=$token",
            "#token=$token", "?token=$token#fragment", "?token=%XX")) {
            assertNull(parser.token("https://saludya.test/reset-password$suffix", "saludya.test"))
        }
    }
    @Test fun `URL encoded token is decoded exactly once`() {
        assertEquals(token, parser.token("https://saludya.test/reset-password?token=%41${"A".repeat(42)}", "saludya.test"))
        assertNull(parser.token("https://saludya.test/reset-password?token=%2541${"A".repeat(42)}", "saludya.test"))
    }
}
