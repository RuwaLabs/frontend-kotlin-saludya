package pe.edu.upc.saludya.features.configuration.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import pe.edu.upc.saludya.features.configuration.infrastructure.repositories.LinkedMinorRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LinkedMinorRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLinkedMinorRepository(
        impl: LinkedMinorRepositoryImpl,
    ): LinkedMinorRepository
}