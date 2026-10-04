package pe.edu.upc.saludya.features.configuration.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.configuration.domain.repository.ConfigurationRepository
import pe.edu.upc.saludya.features.configuration.infrastructure.repositories.ConfigurationRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigurationRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConfigurationRepository(
        impl: ConfigurationRepositoryImpl,
    ): ConfigurationRepository
}