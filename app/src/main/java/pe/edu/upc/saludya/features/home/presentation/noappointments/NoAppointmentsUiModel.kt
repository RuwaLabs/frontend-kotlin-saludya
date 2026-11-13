package pe.edu.upc.saludya.features.home.presentation.noappointments

data class NoAppointmentsUiModel(
    val title: String = "Citas pendientes",
    val heading: String = "Aún no tienes citas",
    val description: String = "Reserva una cita para ti o para un menor vinculado. Aquí podrás consultar sus detalles.",
    val buttonText: String = "Reservar cita",
)
