package pe.edu.upc.saludya.features.configuration.domain.entity

data class SpecialtyDemand(
    val dateLabel: String,
    val totalReservations: Int,
    val items: List<SpecialtyDemandItem>,
)

data class SpecialtyDemandItem(
    val specialtyName: String,
    val reservations: Int,
)