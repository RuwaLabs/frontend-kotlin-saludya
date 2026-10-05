package pe.edu.upc.saludya.features.configuration.domain.entity

data class DailyIndicators(
    val dateLabel: String,
    val centerName: String,
    val programmed: Int,
    val pending: Int,
    val cancelled: Int,
    val noShow: Int,
    val attendedAppointments: Int,
    val recoveredSlots: Int,
    val lastUpdatedLabel: String,
)