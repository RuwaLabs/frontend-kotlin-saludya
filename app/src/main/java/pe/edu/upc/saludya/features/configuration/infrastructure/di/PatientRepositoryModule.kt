package pe.edu.upc.saludya.features.configuration.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.configuration.domain.repository.PatientRepository
import pe.edu.upc.saludya.features.configuration.infrastructure.repositories.PatientRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PatientRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPatientRepository(
        impl: PatientRepositoryImpl,
    ): PatientRepository
}