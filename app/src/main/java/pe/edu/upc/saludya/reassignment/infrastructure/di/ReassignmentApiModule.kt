package pe.edu.upc.saludya.reassignment.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import pe.edu.upc.saludya.reassignment.infrastructure.remote.ReassignmentOfferService
import retrofit2.Retrofit
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object ReassignmentApiModule {
    @Provides
    @Singleton
    fun provideService(retrofit: Retrofit): ReassignmentOfferService =
        retrofit.create(ReassignmentOfferService::class.java)
}