package pe.edu.upc.saludya.features.patientreservations.infrastructure.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.AppointmentRepository
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.DoctorRepository
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.SpecialityRepository
import pe.edu.upc.saludya.features.patientreservations.domain.repositories.TimeSlotRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories.AppointmentRepositoryImpl
import pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories.DoctorRepositoryImpl
import pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories.SpecialityRepositoryImpl
import pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories.TimeSlotRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PatientReservationRepositoryModule {

    @Binds
    @Singleton
    fun bindAppointmentRepository(impl: AppointmentRepositoryImpl): AppointmentRepository

    @Binds
    @Singleton
    fun bindSpecialityRepository(impl: SpecialityRepositoryImpl): SpecialityRepository

    @Binds
    @Singleton
    fun bindDoctorRepository(impl: DoctorRepositoryImpl): DoctorRepository

    @Binds
    @Singleton
    fun bindTimeSlotRepository(impl: TimeSlotRepositoryImpl): TimeSlotRepository
}
