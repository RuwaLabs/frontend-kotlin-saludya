package pe.edu.upc.saludya.features.home.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.home.infrastructure.local.AppDatabase
import pe.edu.upc.saludya.features.home.infrastructure.local.HomeAppointmentDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeLocalModule {

    @Provides
    @Singleton
    fun provideHomeAppointmentDao(database: AppDatabase): HomeAppointmentDao =
        database.homeAppointmentDao()
}
