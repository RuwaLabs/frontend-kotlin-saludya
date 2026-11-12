package pe.edu.upc.saludya.features.configuration.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.configuration.infrastructure.remote.PatientApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConfigurationNetworkModule {

    @Provides
    @Singleton
    fun providePatientApi(
        retrofit: Retrofit,
    ): PatientApi {
        return retrofit.create(PatientApi::class.java)
    }
}