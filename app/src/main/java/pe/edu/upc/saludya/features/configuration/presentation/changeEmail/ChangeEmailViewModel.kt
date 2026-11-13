package pe.edu.upc.saludya.features.configuration.presentation.changeEmail

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.core.session.SessionProvider
import pe.edu.upc.saludya.features.iam.infrastructure.remote.AccessApi
import pe.edu.upc.saludya.features.iam.infrastructure.remote.UpdateContactRequest
import pe.edu.upc.saludya.features.patientreservations.infrastructure.remote.PatientService
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val session: SessionProvider,
    private val accounts: AccessApi,
    private val patients: PatientService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChangeEmailUiState>(ChangeEmailUiState.Editing())
    val uiState: StateFlow<ChangeEmailUiState> = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        val current = _uiState.value
        if (current is ChangeEmailUiState.Editing) {
            _uiState.value = current.copy(newEmail = newValue)
        }
    }

    fun save() {
        val current = _uiState.value
        if (current !is ChangeEmailUiState.Editing) return

        val email = current.newEmail.trim()

        // Validación de formato de email
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = ChangeEmailUiState.InvalidInput(
                R.string.error_invalid_email_or_phone
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSending = true)
            try {
                val token = session.accessToken ?: error("No hay sesión")
                val accountResponse = accounts.profile("Bearer $token")
                val account = accountResponse.body() ?: error("No se pudo cargar la cuenta")
                val patientId = session.patientId ?: error("No hay paciente")
                if (account.patientId != patientId.toLong()) error("Cuenta incorrecta")
                val patient = patients.getPatient(patientId).body() ?: error("No se pudo cargar el teléfono")
                val response = accounts.updateContact(
                    account.id ?: error("No hay cuenta"),
                    UpdateContactRequest(email, patient.phone ?: error("No hay teléfono")),
                )
                if (!response.isSuccessful) error("No se pudo guardar el correo")
                _uiState.value = ChangeEmailUiState.Saved
            } catch (_: Exception) {
                _uiState.value = ChangeEmailUiState.Error(R.string.error_generic)
            }
        }
    }

    fun dismissInvalidInput() {
        val current = _uiState.value
        if (current is ChangeEmailUiState.InvalidInput) {
            _uiState.value = ChangeEmailUiState.Editing()
        }
    }
    fun retry() { _uiState.value = ChangeEmailUiState.Editing() }
}
