package pe.edu.upc.saludya.iam.domain.model

import java.util.Locale

data class CredentialsForm(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val phone: String = ""
) {
    override fun toString(): String = "CredentialsForm[REDACTED]"
}

data class RegistrationCredentials(val email: String, val password: String, val phone: String) {
    override fun toString(): String = "RegistrationCredentials[REDACTED]"
}

enum class CredentialsField { EMAIL, PASSWORD, CONFIRM_PASSWORD, PHONE }
enum class CredentialsError { EMAIL_FORMAT, EMAIL_IN_USE, PASSWORD_POLICY, PASSWORD_MISMATCH, PHONE_FORMAT }

data class CredentialsValidation(
    val credentials: RegistrationCredentials?,
    val errors: Map<CredentialsField, CredentialsError>
)

object CredentialsValidator {
    fun validate(form: CredentialsForm): CredentialsValidation {
        val errors = mutableMapOf<CredentialsField, CredentialsError>()
        val email = form.email.trim().lowercase(Locale.ROOT)
        if (email.length > 150 || !email.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))) {
            errors[CredentialsField.EMAIL] = CredentialsError.EMAIL_FORMAT
        }
        // Same password policy as the backend's BCrypt adapter. Never trim a password.
        if (!AccessValidation.password(form.password)) {
            errors[CredentialsField.PASSWORD] = CredentialsError.PASSWORD_POLICY
        }
        if (form.password != form.confirmPassword) {
            errors[CredentialsField.CONFIRM_PASSWORD] = CredentialsError.PASSWORD_MISMATCH
        }
        if (!form.phone.matches(Regex("[0-9]{9}"))) {
            errors[CredentialsField.PHONE] = CredentialsError.PHONE_FORMAT
        }
        return CredentialsValidation(
            if (errors.isEmpty()) RegistrationCredentials(email, form.password, form.phone) else null,
            errors
        )
    }
}
