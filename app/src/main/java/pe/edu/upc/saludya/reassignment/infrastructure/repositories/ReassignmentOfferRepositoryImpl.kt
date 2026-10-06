package pe.edu.upc.saludya.reassignment.infrastructure.repositories

import pe.edu.upc.saludya.reassignment.domain.ReassignmentOffer
import pe.edu.upc.saludya.reassignment.domain.ReassignmentOfferRepository
import pe.edu.upc.saludya.reassignment.infrastructure.remote.ReassignmentOfferService
import javax.inject.Inject

class ReassignmentOfferRepositoryImpl @Inject constructor(
    private val service: ReassignmentOfferService
): ReassignmentOfferRepository {
    override suspend fun getPending(appointmentId: Long?): List<ReassignmentOffer> {
        try {
            val response = service.getPending(appointmentId)

            if (response.isSuccessful) {
                response.body()?.let { responseDto ->
                    val entities = responseDto.pending.map { dto ->
                        ReassignmentOffer(
                            dto.id,
                            dto.appointmentId,
                            dto.freedTimeSlotId,
                            dto.status,
                            dto.expiresAt
                        )
                    }.toList()
                }
            }
        } catch (e: Exception) {

        }

        return emptyList()
    }

    override suspend fun accept(id: Long): ReassignmentOffer? {
        try {

            val entity = service.accept(id)
            return ReassignmentOffer(
                entity.id,
                entity.appointmentId,
                entity.freedTimeSlotId,
                entity.status,
                entity.expiresAt
            )

        } catch (e: Exception) {

        }

        return null
    }

    override suspend fun reject(id: Long): ReassignmentOffer? {
        try {

            val entity = service.reject(id)
            return ReassignmentOffer(
                entity.id,
                entity.appointmentId,
                entity.freedTimeSlotId,
                entity.status,
                entity.expiresAt
            )

        } catch (e: Exception) {

        }

        return null
    }
}