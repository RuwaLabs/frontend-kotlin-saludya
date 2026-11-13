package pe.edu.upc.saludya.reservations

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.features.patientreservations.domain.model.TimeslotConflictException
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.repositories.AppointmentRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppointmentRepositoryErrorsTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: AppointmentRepositoryImpl

    @Before fun setUp() {
        server = MockWebServer()
        server.start()
        val service = Retrofit.Builder().baseUrl(server.url("/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(AppointmentService::class.java)
        repository = AppointmentRepositoryImpl(service)
    }

    @After fun tearDown() { server.shutdown() }

    @Test fun `cancellation deadline is explained in Spanish`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(422).setBody(
                """{"code":"BUSINESS_RULE_VIOLATION","message":"The operation violates a business rule","details":"The cancellation deadline has passed; cancel in person with admission staff"}"""
            )
        )
        val error = repository.cancelAppointment(2).exceptionOrNull()
        assertEquals(
            "No se pudo cancelar: ya pasó el plazo de 24 horas antes de la cita. Acércate a admisión.",
            error?.message,
        )
    }

    @Test fun `past time slot is explained in Spanish`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(422).setBody(
                """{"code":"BUSINESS_RULE_VIOLATION","message":"The operation violates a business rule","details":"The selected time slot is in the past"}"""
            )
        )
        val error = repository.bookAppointment(2, 7).exceptionOrNull()
        assertEquals(
            "No se pudo reservar: el servidor considera que ese horario ya pasó. Elige un horario más tarde.",
            error?.message,
        )
    }

    @Test fun `full slot keeps the occupied slot dialog`() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(409).setBody(
                """{"code":"TIMESLOT_CONFLICT","message":"conflict","details":"Time slot 14 has no available capacity"}"""
            )
        )
        assertTrue(repository.bookAppointment(2, 14).exceptionOrNull() is TimeslotConflictException)
    }
}
