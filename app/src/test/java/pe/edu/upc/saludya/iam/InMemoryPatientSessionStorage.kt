package pe.edu.upc.saludya.features.iam

import pe.edu.upc.saludya.features.iam.domain.repository.PatientSession
import pe.edu.upc.saludya.features.iam.domain.repository.PatientSessionStorage

class InMemoryPatientSessionStorage : PatientSessionStorage {
    var session: PatientSession? = null

    override suspend fun save(session: PatientSession) {
        this.session = session
    }

    override suspend fun read(): PatientSession? = session

    override suspend fun clear() {
        session = null
    }
}
