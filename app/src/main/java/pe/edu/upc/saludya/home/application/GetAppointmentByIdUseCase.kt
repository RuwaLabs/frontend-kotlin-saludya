package pe.edu.upc.saludya.home.application

import pe.edu.upc.saludya.home.domain.HomeAppointment
import pe.edu.upc.saludya.home.domain.HomeRepository
import javax.inject.Inject

class GetAppointmentByIdUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: Long): HomeAppointment? = repository.getById(id)
}
