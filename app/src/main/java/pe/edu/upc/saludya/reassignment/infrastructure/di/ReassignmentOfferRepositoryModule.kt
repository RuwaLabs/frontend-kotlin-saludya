package pe.edu.upc.saludya.reassignment.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.reassignment.domain.ReassignmentOfferRepository
import pe.edu.upc.saludya.reassignment.infrastructure.repositories.ReassignmentOfferRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface ReassignmentOfferRepositoryModule {

    @Binds
    fun provideReassignmentOfferRepository(impl: ReassignmentOfferRepositoryImpl):
            ReassignmentOfferRepository
}