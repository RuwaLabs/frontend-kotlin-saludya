package pe.edu.upc.saludya.features.iam.domain.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.core.session.SessionState
import pe.edu.upc.saludya.features.iam.domain.repository.PatientSession
import pe.edu.upc.saludya.features.iam.domain.repository.PatientSessionStorage
import java.time.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientSessionStore @Inject constructor(
    private val clock: Clock,
    private val sessionProvider: SessionProvider,
    private val storage: PatientSessionStorage,
) {
    private val persistenceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutableSession = MutableStateFlow<PatientSession?>(null)
    val session = mutableSession.asStateFlow()
    private val mutableExpired = MutableStateFlow(false)
    val expired = mutableExpired.asStateFlow()
    private var explicitlyClosed = false

    /** Lee la sesión guardada en el dispositivo y la restaura si sigue vigente. */
    suspend fun restore(): PatientSession? {
        if (explicitlyClosed) return null
        current()?.let { return it }
        val stored = storage.read() ?: return null
        if (!stored.expiresAt.isAfter(clock.instant())) {
            storage.clear()
            return null
        }
        mutableSession.value = stored
        mutableExpired.value = false
        sessionProvider.update(SessionState(stored.accessToken, stored.patientId.toInt()))
        return stored
    }

    fun open(session: PatientSession): Boolean {
        if (!session.expiresAt.isAfter(clock.instant())) return false
        explicitlyClosed = false
        mutableSession.value = session
        mutableExpired.value = false
        sessionProvider.update(SessionState(session.accessToken, session.patientId.toInt()))
        persistenceScope.launch { storage.save(session) }
        return true
    }

    fun current(): PatientSession? {
        val value = mutableSession.value ?: return null
        if (!value.expiresAt.isAfter(clock.instant())) {
            expire()
            return null
        }
        return value
    }

    fun expire() {
        explicitlyClosed = true
        mutableSession.value = null
        mutableExpired.value = true
        sessionProvider.clear()
        persistenceScope.launch { storage.clear() }
    }

    fun expireIfToken(token: String) {
        if (mutableSession.value?.accessToken == token) expire()
    }

    fun clear() {
        explicitlyClosed = true
        mutableSession.value = null
        mutableExpired.value = false
        sessionProvider.clear()
        persistenceScope.launch { storage.clear() }
    }
}
