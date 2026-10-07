package pe.edu.upc.saludya.notifications.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.notifications.domain.NotificationRepository
import pe.edu.upc.saludya.notifications.infrastructure.repositories.InMemoryNotificationRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        impl: InMemoryNotificationRepositoryImpl
    ): NotificationRepository
}
