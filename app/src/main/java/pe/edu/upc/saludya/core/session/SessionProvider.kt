package pe.edu.upc.saludya.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

data class SessionState(
    val accessToken: String,
    val patientId: Int
)

/**
 * Sesión del paciente expuesta a los features. El feature de IAM la actualiza
 * al iniciar sesión y la limpia al cerrarla.
 */
@Singleton
class SessionProvider @Inject constructor() {

    private val _session = MutableStateFlow<SessionState?>(null)
    val session: StateFlow<SessionState?> = _session.asStateFlow()

    val accessToken: String?
        get() = _session.value?.accessToken

    val patientId: Int?
        get() = _session.value?.patientId

    fun update(state: SessionState?) {
        _session.value = state
    }

    fun clear() {
        _session.value = null
    }
}
