package pe.edu.upc.saludya.iam.domain.model

import java.util.Locale

object AccessValidation {
    fun password(value: String): Boolean = value.length >= 8 && value.toByteArray(Charsets.UTF_8).size <= 72 &&
        value.any { it in 'a'..'z' || it in 'A'..'Z' } && value.any { it in '0'..'9' }
    fun email(value: String): String? = value.trim().lowercase(Locale.ROOT).takeIf {
        it.length <= 150 && it.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
    }
    fun code(value: String): Boolean = value.matches(Regex("[0-9]{6}"))
}
