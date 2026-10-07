package pe.edu.upc.saludya.iam.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

data class IdentityForm(
    val dni: String = "",
    val names: String = "",
    val lastNames: String = "",
    val birthDate: String = ""
)

data class IdentityDetails(
    val dni: String,
    val names: String,
    val lastNames: String,
    val birthDate: LocalDate
)

/** Only the backend's positive response may produce a verified identity. */
data class VerifiedIdentity(val details: IdentityDetails)

enum class IdentityField { DNI, NAMES, LAST_NAMES, BIRTH_DATE }
enum class IdentityValidationError { DNI_FORMAT, REQUIRED, NAME_TOO_LONG, DATE_FORMAT, DATE_NOT_PAST }

data class IdentityValidation(
    val details: IdentityDetails?,
    val errors: Map<IdentityField, IdentityValidationError>
)

object IdentityValidator {
    private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    fun validate(form: IdentityForm, today: LocalDate): IdentityValidation {
        val errors = mutableMapOf<IdentityField, IdentityValidationError>()
        // Do not truncate or remove non-numeric characters: invalid input must be rejected.
        if (!form.dni.matches(Regex("[0-9]{8}"))) {
            errors[IdentityField.DNI] = IdentityValidationError.DNI_FORMAT
        }
        for ((field, value) in listOf(IdentityField.NAMES to form.names, IdentityField.LAST_NAMES to form.lastNames)) {
            if (value.isBlank()) errors[field] = IdentityValidationError.REQUIRED
            else if (value.trim().length > 100) errors[field] = IdentityValidationError.NAME_TOO_LONG
        }
        val date = runCatching { LocalDate.parse(form.birthDate.trim(), dateFormat) }.getOrNull()
        if (date == null) errors[IdentityField.BIRTH_DATE] = IdentityValidationError.DATE_FORMAT
        else if (!date.isBefore(today)) errors[IdentityField.BIRTH_DATE] = IdentityValidationError.DATE_NOT_PAST
        val details = if (errors.isEmpty() && date != null) {
            IdentityDetails(form.dni, form.names.trim(), form.lastNames.trim(), date)
        } else null
        return IdentityValidation(details, errors)
    }
}
