package pe.edu.upc.saludya.features.home.infrastructure.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface HomeAppointmentDao {

    @Query("SELECT * FROM home_appointments WHERE isUpcoming = 1 ORDER BY id")
    suspend fun getUpcoming(): List<HomeAppointmentEntity>

    @Query("SELECT * FROM home_appointments WHERE isUpcoming = 0 ORDER BY id DESC")
    suspend fun getHistory(): List<HomeAppointmentEntity>

    @Query("SELECT * FROM home_appointments WHERE id = :id")
    suspend fun getById(id: Long): HomeAppointmentEntity?

    @Upsert
    suspend fun upsertAll(items: List<HomeAppointmentEntity>)

    @Query("SELECT COUNT(*) FROM home_appointments")
    suspend fun count(): Int
}
