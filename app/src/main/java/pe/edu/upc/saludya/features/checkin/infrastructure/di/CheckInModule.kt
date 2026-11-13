package pe.edu.upc.saludya.features.checkin.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository
import pe.edu.upc.saludya.features.checkin.infrastructure.repositories.RemoteCheckInRepository
import pe.edu.upc.saludya.features.checkin.infrastructure.remote.CheckInApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckInModule {

    @Provides
    @Singleton
    fun provideCheckInRepository(repository: RemoteCheckInRepository): CheckInRepository = repository

    @Provides
    @Singleton
    fun provideCheckInApi(retrofit: Retrofit): CheckInApi = retrofit.create(CheckInApi::class.java)
}
