package pe.edu.upc.saludya.features.reassignment.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.reassignment.infrastructure.remote.ReassignmentOfferService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReassignmentOfferApiModule {
    @Provides
    @Singleton
    fun provideReassignmentOfferService(retrofit: Retrofit): ReassignmentOfferService =
        retrofit.create(ReassignmentOfferService::class.java)
}