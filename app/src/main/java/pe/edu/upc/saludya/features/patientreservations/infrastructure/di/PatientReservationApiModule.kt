package pe.edu.upc.saludya.features.patientreservations.infrastructure.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PatientReservationApiModule {

    @Provides
    @Singleton
    fun provideAppointmentService(retrofit: Retrofit): AppointmentService =
        retrofit.create(AppointmentService::class.java)

    @Provides
    @Singleton
    fun provideSpecialityService(retrofit: Retrofit): SpecialityService =
        retrofit.create(SpecialityService::class.java)

    @Provides
    @Singleton
    fun provideDoctorService(retrofit: Retrofit): DoctorService =
        retrofit.create(DoctorService::class.java)

    @Provides
    @Singleton
    fun provideTimeSlotService(retrofit: Retrofit): TimeSlotService =
        retrofit.create(TimeSlotService::class.java)

    @Provides
    @Singleton
    fun providePatientService(retrofit: Retrofit): PatientService =
        retrofit.create(PatientService::class.java)
}
