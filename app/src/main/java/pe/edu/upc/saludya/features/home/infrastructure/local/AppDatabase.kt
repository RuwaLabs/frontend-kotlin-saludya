package pe.edu.upc.saludya.features.home.infrastructure.local

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [HomeAppointmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeAppointmentDao(): HomeAppointmentDao
}
