package pe.edu.upc.saludya.home.application

import pe.edu.upc.saludya.home.domain.HomeAppointment
import pe.edu.upc.saludya.home.domain.HomeRepository
import javax.inject.Inject

class GetUpcomingAppointmentsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<HomeAppointment> = repository.getUpcoming()
}
