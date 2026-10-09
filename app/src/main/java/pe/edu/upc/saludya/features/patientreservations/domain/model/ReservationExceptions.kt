package pe.edu.upc.saludya.features.patientreservations.domain.model

/** Conflicto: otra reserva tomó el cupo (HTTP 409 · TIMESLOT_CONFLICT). */
class TimeslotConflictException : Exception()

/** Conflicto: el beneficiario ya tiene una cita en ese bloque (HTTP 409 · APPOINTMENT_CONFLICT). */
class AppointmentConflictException : Exception()
