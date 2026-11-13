package pe.edu.upc.saludya.features.configuration.presentation.changePassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.iam.domain.model.AccessValidation
import pe.edu.upc.saludya.features.iam.domain.model.PatientSessionStore
import pe.edu.upc.saludya.features.iam.domain.repository.AccessFailure
import pe.edu.upc.saludya.features.iam.domain.repository.AccessResult
import pe.edu.upc.saludya.features.iam.domain.repository.RecoveryActionsRepository
import pe.edu.upc.saludya.features.iam.infrastructure.remote.AccessApi
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val accounts: AccessApi,
    private val recovery: RecoveryActionsRepository,
    private val session: SessionProvider,
    private val sessions: PatientSessionStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private var challengeId: String? = null
    private var resetToken: String? = null

    init {
        loadEmail()
    }

    fun loadEmail() {
        val token = session.accessToken
        if (token.isNullOrBlank()) {
            _uiState.update {
                it.copy(step = ChangePasswordStep.ERROR, error = "Inicia sesión nuevamente.")
            }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(step = ChangePasswordStep.LOADING, error = null) }
            val response = accounts.profile("Bearer $token")
            val email = response.body()?.email
            if (!response.isSuccessful || email.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        step = ChangePasswordStep.ERROR,
                        error = "No se pudo obtener el correo de tu cuenta.",
                    )
                }
            } else {
                _uiState.update {
                    it.copy(step = ChangePasswordStep.READY, email = email, error = null)
                }
            }
        }
    }

    fun editCode(value: String) {
        _uiState.update { it.copy(code = value.filter { char -> char.isDigit() }.take(6), error = null) }
    }

    fun editPassword(value: String) {
        _uiState.update { it.copy(password = value, error = null) }
    }

    fun editConfirmation(value: String) {
        _uiState.update { it.copy(confirmation = value, error = null) }
    }

    fun sendCode() {
        val current = _uiState.value
        if (current.busy || current.email.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null, message = null) }
            when (val result = recovery.requestRecoveryCode(current.email)) {
                is AccessResult.Success -> {
                    challengeId = result.value.challengeId
                    _uiState.update {
                        it.copy(
                            step = ChangePasswordStep.CODE_SENT,
                            maskedEmail = result.value.maskedEmail,
                            busy = false,
                            message = "Código enviado a ${result.value.maskedEmail}.",
                        )
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    fun verifyCode() {
        val current = _uiState.value
        val challenge = challengeId ?: return
        if (current.busy) return
        if (!AccessValidation.code(current.code)) {
            _uiState.update { it.copy(error = "Ingresa el código de 6 dígitos.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null) }
            when (val result = recovery.verifyRecoveryCode(challenge, current.code)) {
                is AccessResult.Success -> {
                    resetToken = result.value.resetToken
                    _uiState.update {
                        it.copy(step = ChangePasswordStep.VERIFIED, busy = false, message = null)
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    fun save() {
        val current = _uiState.value
        val token = resetToken ?: return
        if (current.busy) return
        if (!AccessValidation.password(current.password)) {
            _uiState.update { it.copy(error = "La contraseña debe tener al menos 8 caracteres, una letra y un número.") }
            return
        }
        if (current.password != current.confirmation) {
            _uiState.update { it.copy(error = "Las contraseñas no coinciden.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(busy = true, error = null) }
            when (val result = recovery.resetPasswordWithGrant(token, current.password, current.confirmation)) {
                is AccessResult.Success -> {
                    sessions.clear()
                    _uiState.update {
                        it.copy(
                            step = ChangePasswordStep.SAVED,
                            busy = false,
                            password = "",
                            confirmation = "",
                        )
                    }
                }
                is AccessResult.Failure -> _uiState.update {
                    it.copy(busy = false, error = failureMessage(result.reason))
                }
            }
        }
    }

    private fun failureMessage(failure: AccessFailure): String = when (failure) {
        AccessFailure.INVALID_CODE -> "El código es incorrecto o venció."
        AccessFailure.EXPIRED_CODE -> "El código venció. Solicita uno nuevo."
        AccessFailure.TOO_MANY_ATTEMPTS -> "Demasiados intentos. Espera un momento."
        AccessFailure.PASSWORD_MISMATCH -> "Las contraseñas no coinciden."
        AccessFailure.RESET_LINK_INVALID -> "El código ya no es válido. Solicita uno nuevo."
        AccessFailure.UNAVAILABLE -> "No se pudo conectar con el servidor. Intenta nuevamente."
        else -> "No se pudo completar la operación. Intenta nuevamente."
    }
}
