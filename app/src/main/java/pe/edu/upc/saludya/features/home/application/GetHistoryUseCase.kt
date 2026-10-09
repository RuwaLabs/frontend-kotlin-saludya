package pe.edu.upc.saludya.features.home.application

import pe.edu.upc.saludya.features.home.domain.HomeAppointment
import pe.edu.upc.saludya.features.home.domain.HomeRepository
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): List<HomeAppointment> = repository.getHistory()
}
