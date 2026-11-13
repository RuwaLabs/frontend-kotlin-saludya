package pe.edu.upc.saludya.features.patientreservations.domain.model

data class Doctor(
    val id : Int,
    val specialityId : Int,
    val name : String,
    val lastname : String
)
