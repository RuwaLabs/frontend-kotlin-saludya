package pe.edu.upc.saludya.checkin

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.edu.upc.saludya.features.checkin.domain.model.AppointmentStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.model.CheckIn
import pe.edu.upc.saludya.features.checkin.domain.model.CheckInStatus
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AttendanceTicketTest {

    private fun ticket(status: AppointmentStatus): AttendanceTicket = AttendanceTicket(
        code = "T-001",
        reservation = Reservation(
            id = 1,
            timeSlotId = 1,
            bookingOrder = 1,
            status = status,
            patientFullName = "Paciente",
            specialty = "Medicina General",
            doctorFullName = "Carlos Vega",
            date = LocalDate.now(),
            startHour = LocalTime.NOON,
            endHour = LocalTime.NOON.plusMinutes(30),
        ),
        checkIn = CheckIn(
            id = 1,
            appointmentId = 1,
            qrToken = null,
            status = CheckInStatus.VALID,
            checkedInAt = LocalDateTime.now(),
        ),
    )

    @Test fun `attended appointment is shown as attended`() {
        assertTrue(ticket(AppointmentStatus.ATTENDED).isAttended)
    }

    @Test fun `active appointment is not shown as attended`() {
        assertFalse(ticket(AppointmentStatus.CONFIRMED).isAttended)
    }
}
