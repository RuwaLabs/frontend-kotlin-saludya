package pe.edu.upc.saludya.features.configuration.presentation.changePassword

enum class ChangePasswordStep { LOADING, READY, CODE_SENT, VERIFIED, SAVED, ERROR }

data class ChangePasswordUiState(
    val step: ChangePasswordStep = ChangePasswordStep.LOADING,
    val email: String = "",
    val maskedEmail: String = "",
    val code: String = "",
    val password: String = "",
    val confirmation: String = "",
    val busy: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)
