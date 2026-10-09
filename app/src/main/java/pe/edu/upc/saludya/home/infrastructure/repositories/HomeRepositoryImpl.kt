package pe.edu.upc.saludya.home.infrastructure.repositories

import pe.edu.upc.saludya.home.domain.HomeAppointment
import pe.edu.upc.saludya.home.domain.HomeRepository
import pe.edu.upc.saludya.home.infrastructure.local.HomeAppointmentDao
import pe.edu.upc.saludya.home.infrastructure.local.HomeSeedData
import pe.edu.upc.saludya.home.infrastructure.local.toDomain
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dao: HomeAppointmentDao
) : HomeRepository {

    override suspend fun getUpcoming(): List<HomeAppointment> {
        seedIfEmpty()
        return dao.getUpcoming().map { it.toDomain() }
    }

    override suspend fun getHistory(): List<HomeAppointment> {
        seedIfEmpty()
        return dao.getHistory().map { it.toDomain() }
    }

    override suspend fun getById(id: Long): HomeAppointment? {
        seedIfEmpty()
        return dao.getById(id)?.toDomain()
    }

    private suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.upsertAll(HomeSeedData.appointments)
        }
    }
}
