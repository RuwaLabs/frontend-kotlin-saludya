package pe.edu.upc.saludya.iam.domain.model

import java.util.Locale

object AccessValidation {
    fun email(value: String): String? = value.trim().lowercase(Locale.ROOT).takeIf {
        it.length <= 150 && it.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
    }
    fun code(value: String): Boolean = value.matches(Regex("[0-9]{6}"))
}
