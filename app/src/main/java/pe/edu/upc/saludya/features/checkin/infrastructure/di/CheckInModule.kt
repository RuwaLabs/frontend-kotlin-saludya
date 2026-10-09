package pe.edu.upc.saludya.features.checkin.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository
import pe.edu.upc.saludya.features.checkin.infrastructure.repositories.InMemoryCheckInRepository
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CheckInModule {

    @Provides
    @Singleton
    fun provideCheckInRepository(): CheckInRepository =
        InMemoryCheckInRepository { LocalDateTime.now() }
}
