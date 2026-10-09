package pe.edu.upc.saludya.features.iam

import org.junit.Assert.*
import org.junit.Test
import pe.edu.upc.saludya.features.iam.domain.model.*
import java.time.LocalDate

class IdentityValidatorTest {
    private val today = LocalDate.of(2026, 10, 7)
    private val valid = IdentityForm("01234567", "Lucía", "Torres", "29/02/2000")

    @Test fun `valid data keeps leading zero and parses date strictly`() {
        val result = IdentityValidator.validate(valid, today)
        assertTrue(result.errors.isEmpty())
        assertEquals("01234567", result.details?.dni)
        assertEquals(LocalDate.of(2000, 2, 29), result.details?.birthDate)
    }

    @Test fun `invalid DNI is rejected rather than filtered or truncated`() {
        for (dni in listOf("", "1234567", "123456789", "1234567a", "1234 567", "１２３４５６７８")) {
            val result = IdentityValidator.validate(valid.copy(dni = dni), today)
            assertNull(result.details)
            assertEquals(IdentityValidationError.DNI_FORMAT, result.errors[IdentityField.DNI])
        }
    }

    @Test fun `impossible or incorrectly formatted date is rejected`() {
        for (date in listOf("31/02/2000", "29/02/2001", "2000-01-01", "")) {
            assertEquals(IdentityValidationError.DATE_FORMAT,
                IdentityValidator.validate(valid.copy(birthDate = date), today).errors[IdentityField.BIRTH_DATE])
        }
    }

    @Test fun `today and future birth dates are rejected`() {
        for (date in listOf("07/10/2026", "08/10/2026")) {
            assertEquals(IdentityValidationError.DATE_NOT_PAST,
                IdentityValidator.validate(valid.copy(birthDate = date), today).errors[IdentityField.BIRTH_DATE])
        }
    }

    @Test fun `blank names and oversized last names are rejected`() {
        val result = IdentityValidator.validate(valid.copy(names = " ", lastNames = "a".repeat(101)), today)
        assertEquals(IdentityValidationError.REQUIRED, result.errors[IdentityField.NAMES])
        assertEquals(IdentityValidationError.NAME_TOO_LONG, result.errors[IdentityField.LAST_NAMES])
        assertNull(result.details)
    }
}
