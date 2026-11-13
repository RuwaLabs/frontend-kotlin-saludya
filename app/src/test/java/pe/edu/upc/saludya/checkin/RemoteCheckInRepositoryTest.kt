package pe.edu.upc.saludya.checkin

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.checkin.infrastructure.remote.CheckInApi
import pe.edu.upc.saludya.features.checkin.infrastructure.repositories.RemoteCheckInRepository
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.AppointmentService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.DoctorService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.SpecialityService
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.TimeSlotService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.ZoneId

class RemoteCheckInRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: RemoteCheckInRepository

    @Before fun setUp() {
        server = MockWebServer()
        server.start()
        val retrofit = Retrofit.Builder().baseUrl(server.url("/api/v1/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
        repository = RemoteCheckInRepository(
            retrofit.create(CheckInApi::class.java),
            retrofit.create(AppointmentService::class.java),
            retrofit.create(TimeSlotService::class.java),
            retrofit.create(DoctorService::class.java),
            retrofit.create(SpecialityService::class.java),
            retrofit.create(PatientService::class.java),
            SessionProvider(),
        )
    }

    @After fun tearDown() { server.shutdown() }

    private fun enqueueAppointment() {
        server.enqueue(
            MockResponse().setBody(
                """{"id":42,"timeSlotId":1,"patientId":2,"bookingOrder":1,"bookingCode":"SAL-X","status":"RESERVED","createdAt":"2026-11-13T10:00:00Z","updatedAt":"2026-11-13T10:00:00Z"}"""
            )
        )
    }

    private fun enqueueSlot(date: LocalDate) {
        server.enqueue(
            MockResponse().setBody(
                """{"id":1,"doctorId":1,"date":"$date","startHour":"09:00:00","endHour":"09:30:00","room":"Consultorio 1","maxCapacity":1,"currentBookings":0,"status":"AVAILABLE"}"""
            )
        )
    }

    @Test fun `queue position comes from backend`() = runTest {
        enqueueAppointment()
        enqueueSlot(LocalDate.now(ZoneId.of("America/Lima")))
        server.enqueue(MockResponse().setBody("""{"position":3,"totalInQueue":8}"""))
        val position = repository.observeQueuePosition(42).first()
        assertEquals(3, position?.current)
        assertEquals(8, position?.total)
        server.takeRequest()
        server.takeRequest()
        assertEquals("/api/v1/check-ins/appointment/42/position", server.takeRequest().path)
    }

    @Test fun `absence of check in does not invent a ticket or queue position`() = runTest {
        server.enqueue(MockResponse().setResponseCode(404))
        assertNull(repository.observeTicket(42).first())
        assertEquals("/api/v1/check-ins/appointment/42", server.takeRequest().path)
        enqueueAppointment()
        enqueueSlot(LocalDate.now(ZoneId.of("America/Lima")))
        server.enqueue(MockResponse().setResponseCode(404))
        assertNull(repository.observeQueuePosition(42).first())
    }

    @Test fun `queue is blocked when the appointment is another day`() = runTest {
        val tomorrow = LocalDate.now(ZoneId.of("America/Lima")).plusDays(1)
        enqueueAppointment()
        enqueueSlot(tomorrow)
        try {
            repository.observeQueuePosition(42).first()
            error("Se esperaba un error de fecha")
        } catch (e: IllegalStateException) {
            assertEquals("El código y la cola solo están disponibles el día de la cita.", e.message)
        }
    }
}
