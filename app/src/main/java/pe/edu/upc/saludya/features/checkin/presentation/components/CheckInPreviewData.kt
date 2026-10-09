package pe.edu.upc.saludya.features.checkin.presentation.components

import pe.edu.upc.saludya.features.checkin.domain.model.AppointmentStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceQueueStatus
import pe.edu.upc.saludya.features.checkin.domain.model.AttendanceTicket
import pe.edu.upc.saludya.features.checkin.domain.model.CheckIn
import pe.edu.upc.saludya.features.checkin.domain.model.CheckInStatus
import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import pe.edu.upc.saludya.features.checkin.domain.model.QueueEntry
import pe.edu.upc.saludya.features.checkin.domain.model.QueueEntryStatus
import pe.edu.upc.saludya.features.checkin.domain.model.QueuePosition
import pe.edu.upc.saludya.features.checkin.domain.model.Reservation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Datos fijos para los `@Preview`; reproducen los valores del diseño de referencia. */
internal object CheckInPreviewData {

    val reservation = Reservation(
        id = 142, timeSlotId = 31, bookingOrder = 3, status = AppointmentStatus.CONFIRMED,
        patientFullName = "Lucía Torres", specialty = "Medicina general",
        doctorFullName = "Ana Ríos", date = LocalDate.of(2026, 10, 6),
        startHour = LocalTime.of(9, 0), endHour = LocalTime.of(9, 30),
        consultingRoom = "02", waitingRoom = "A",
    )

    val qrCode = QrCodeInfo(token = "SY-261006-0142", appointmentId = 142)

    val ticket = AttendanceTicket(
        code = "MG-003",
        reservation = reservation,
        checkIn = CheckIn(
            id = 3, appointmentId = 142, qrToken = qrCode.token,
            status = CheckInStatus.VALID, checkedInAt = LocalDateTime.of(2026, 10, 6, 8, 55),
        ),
        queueEntry = QueueEntry(id = 3, attendanceQueueId = 9, checkInId = 3, position = 3),
    )

    val calledTicket = ticket.copy(
        queueEntry = ticket.queueEntry.copy(
            status = QueueEntryStatus.CALLED,
            calledAt = LocalDateTime.of(2026, 10, 6, 9, 15),
        ),
    )

    val position = QueuePosition(
        current = 3, total = 8,
        queueStatus = AttendanceQueueStatus.OPEN, updatedAt = LocalTime.of(9, 10),
    )
}
