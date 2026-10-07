package pe.edu.upc.saludya.iam.domain.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pe.edu.upc.saludya.iam.domain.repository.PatientSession
import java.time.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PatientSessionStore @Inject constructor(private val clock: Clock) {
    private val mutableSession = MutableStateFlow<PatientSession?>(null)
    val session = mutableSession.asStateFlow()
    private val mutableExpired = MutableStateFlow(false)
    val expired = mutableExpired.asStateFlow()

    fun open(session: PatientSession): Boolean {
        if (!session.expiresAt.isAfter(clock.instant())) return false
        mutableSession.value = session
        mutableExpired.value = false
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

    fun expire() { mutableSession.value = null; mutableExpired.value = true }
    fun expireIfToken(token: String) { if (mutableSession.value?.accessToken == token) expire() }
    fun clear() { mutableSession.value = null; mutableExpired.value = false }
}
