package pe.edu.upc.saludya.features.reassignment.domain

interface ReassignmentOfferRepository {

    suspend fun getPending(appointmentId: Long? = null): List<ReassignmentOffer>
    suspend fun accept(id: Long): ReassignmentOffer?
    suspend fun reject(id: Long): ReassignmentOffer?

}