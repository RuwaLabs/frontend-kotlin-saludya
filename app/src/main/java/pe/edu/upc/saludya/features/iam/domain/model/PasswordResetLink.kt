package pe.edu.upc.saludya.features.iam.domain.model

import java.net.URI
import java.net.URLDecoder
import javax.inject.Inject
import pe.edu.upc.saludya.BuildConfig

class PasswordResetLink @Inject constructor() {
    fun token(value: String, host: String = BuildConfig.RESET_LINK_HOST): String? = runCatching {
        val uri = URI(value.trim())
        if (uri.scheme != "https" || !uri.host.equals(host, ignoreCase = true) || uri.path != "/reset-password" ||
            uri.userInfo != null || uri.fragment != null || uri.port !in listOf(-1, 443)) return null
        val tokens = uri.rawQuery.orEmpty().split('&').mapNotNull { pair ->
            val parts = pair.split('=', limit = 2)
            if (parts.size == 2 && URLDecoder.decode(parts[0], "UTF-8") == "token") URLDecoder.decode(parts[1], "UTF-8") else null
        }
        tokens.singleOrNull()?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{43}")) }
    }.getOrNull()
}
