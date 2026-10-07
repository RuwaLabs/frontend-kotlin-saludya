package pe.edu.upc.saludya.reassignment.infrastructure.repositories

import pe.edu.upc.saludya.reassignment.domain.ReassignmentOffer
import pe.edu.upc.saludya.reassignment.domain.ReassignmentOfferRepository
import pe.edu.upc.saludya.reassignment.infrastructure.remote.ReassignmentOfferDto
import pe.edu.upc.saludya.reassignment.infrastructure.remote.ReassignmentOfferService
import retrofit2.Response
import java.time.Instant
import javax.inject.Inject

class ReassignmentOfferRepositoryImpl @Inject constructor(
    private val service: ReassignmentOfferService
) : ReassignmentOfferRepository {

    override suspend fun getPending(appointmentId: Long?): List<ReassignmentOffer> {
        return try {
            val response = service.getPending(appointmentId)
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() }.orEmpty()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun accept(id: Long): ReassignmentOffer? =
        respond { service.accept(id) }

    override suspend fun reject(id: Long): ReassignmentOffer? =
        respond { service.reject(id) }

    private suspend fun respond(
        call: suspend () -> Response<ReassignmentOfferDto>
    ): ReassignmentOffer? = try {
        val response = call()
        if (response.isSuccessful) response.body()?.toDomain() else null
    } catch (_: Exception) {
        null
    }
}

private fun ReassignmentOfferDto.toDomain(): ReassignmentOffer = ReassignmentOffer(
    id = id,
    appointmentId = appointmentId,
    originalAppointmentId = originalAppointmentId,
    freedTimeSlotId = freedTimeSlotId,
    candidateTimeSlotId = candidateTimeSlotId,
    status = status,
    offeredAt = Instant.parse(offeredAt),
    respondedAt = respondedAt?.let { Instant.parse(it) },
    expiresAt = Instant.parse(expiresAt)
)
