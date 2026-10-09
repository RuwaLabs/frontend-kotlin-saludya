package pe.edu.upc.saludya.features.patientreservations.domain.model

data class Beneficiary(
    val patientId: Int,
    val name: String,
    val lastname: String,
    val dni: String,
    val titular: Boolean
) {
    val displayName: String get() = "$name $lastname"
    val maskedDni: String get() = "••••${dni.takeLast(4)}"
}
