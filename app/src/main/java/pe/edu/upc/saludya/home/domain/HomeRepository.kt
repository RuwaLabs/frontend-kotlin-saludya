package pe.edu.upc.saludya.home.domain

interface HomeRepository {

    suspend fun getUpcoming(): List<HomeAppointment>

    suspend fun getHistory(): List<HomeAppointment>

    suspend fun getById(id: Long): HomeAppointment?
}
