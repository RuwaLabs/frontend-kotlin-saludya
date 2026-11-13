package pe.edu.upc.saludya.features.iam.infrastructure.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import pe.edu.upc.saludya.features.iam.domain.repository.PatientSession
import pe.edu.upc.saludya.features.iam.domain.repository.PatientSessionStorage
import java.time.Instant
import javax.inject.Inject

class DataStorePatientSessionStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PatientSessionStorage {

    override suspend fun save(session: PatientSession) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = session.accessToken
            preferences[EXPIRES_AT] = session.expiresAt.toEpochMilli()
            preferences[USER_ID] = session.userId
            preferences[PATIENT_ID] = session.patientId
        }
    }

    override suspend fun read(): PatientSession? {
        val preferences = dataStore.data.first()
        val token = preferences[TOKEN] ?: return null
        val expiresAt = preferences[EXPIRES_AT] ?: return null
        val userId = preferences[USER_ID] ?: return null
        val patientId = preferences[PATIENT_ID] ?: return null
        return PatientSession(
            accessToken = token,
            expiresAt = Instant.ofEpochMilli(expiresAt),
            userId = userId,
            patientId = patientId,
        )
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private companion object {
        val TOKEN = stringPreferencesKey("access_token")
        val EXPIRES_AT = longPreferencesKey("expires_at")
        val USER_ID = longPreferencesKey("user_id")
        val PATIENT_ID = longPreferencesKey("patient_id")
    }
}
