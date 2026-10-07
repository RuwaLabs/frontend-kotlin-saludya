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

    fun open(session: PatientSession): Boolean {
        if (!session.expiresAt.isAfter(clock.instant())) return false
        mutableSession.value = session
        return true
    }

    fun current(): PatientSession? {
        val value = mutableSession.value ?: return null
        if (!value.expiresAt.isAfter(clock.instant())) {
            clear()
            return null
        }
        return value
    }

    fun clear() { mutableSession.value = null }
}
