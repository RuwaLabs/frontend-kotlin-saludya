package pe.edu.upc.saludya.features.home.infrastructure.local

import pe.edu.upc.saludya.features.home.domain.HomeAppointment

object HomeSeedData {

    val appointments: List<HomeAppointmentEntity> = listOf(
        HomeAppointmentEntity(
            id = 1,
            specialty = "Medicina general",
            patientName = "Lucía Torres · Titular",
            doctorName = "Dra. Ana Ríos",
            date = "Mar 6 oct 2026",
            time = "09:00–09:30",
            place = "Centro de Salud · Consultorio 02",
            status = "Confirmada",
            bookingCode = "SY-261006-0142",
            isUpcoming = true
        ),
        HomeAppointmentEntity(
            id = 2,
            specialty = "Pediatría",
            patientName = "Mateo Torres · Menor vinculado",
            doctorName = "Dra. Carla Díaz",
            date = "Mié 7 oct",
            time = "10:00–10:30",
            place = "Centro de Salud · Consultorio 01",
            status = "Confirmada",
            bookingCode = "SY-261007-0203",
            isUpcoming = true
        ),
        HomeAppointmentEntity(
            id = 3,
            specialty = "Medicina general",
            patientName = "Lucía Torres · Titular",
            doctorName = "Dr. Luis Medina",
            date = "25 sep 2026",
            time = "11:00–11:30",
            place = "Centro de Salud · Consultorio 02",
            status = "Atendida",
            bookingCode = "SY-260925-0098",
            isUpcoming = false
        ),
        HomeAppointmentEntity(
            id = 4,
            specialty = "Odontología",
            patientName = "Lucía Torres · Titular",
            doctorName = "",
            date = "18 sep 2026",
            time = "",
            place = "Centro de Salud · Consultorio 03",
            status = "Cancelada",
            bookingCode = "SY-260918-0071",
            isUpcoming = false
        )
    )
}

fun HomeAppointmentEntity.toDomain(): HomeAppointment = HomeAppointment(
    id = id,
    specialty = specialty,
    patientName = patientName,
    doctorName = doctorName,
    date = date,
    time = time,
    place = place,
    status = status,
    bookingCode = bookingCode,
    isUpcoming = isUpcoming
)
