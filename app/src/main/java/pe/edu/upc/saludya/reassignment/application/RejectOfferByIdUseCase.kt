package pe.edu.upc.saludya.reassignment.application

import pe.edu.upc.saludya.reassignment.domain.ReassignmentOfferRepository
import javax.inject.Inject

class RejectOfferByIdUseCase @Inject constructor(private val repository: ReassignmentOfferRepository) {

    suspend operator fun invoke(id: Long) = repository.reject(id)

}