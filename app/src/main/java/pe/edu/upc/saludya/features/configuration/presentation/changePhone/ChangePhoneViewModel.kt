package pe.edu.upc.saludya.features.configuration.presentation.changePhone

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
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor(
    private val session: SessionProvider,
    private val accounts: AccessApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChangePhoneUiState>(ChangePhoneUiState.Editing())
    val uiState: StateFlow<ChangePhoneUiState> = _uiState.asStateFlow()

    fun onPhoneChange(newValue: String) {
        val current = _uiState.value
        if (current is ChangePhoneUiState.Editing) {
            // Solo dígitos, máximo 9
            val filtered = newValue.filter { it.isDigit() }.take(9)
            _uiState.value = current.copy(newPhone = filtered)
        }
    }

    fun save() {
        val current = _uiState.value
        if (current !is ChangePhoneUiState.Editing) return

        val phone = current.newPhone.trim()

        // Validación: 9 dígitos
        if (phone.length != 9) {
            _uiState.value = ChangePhoneUiState.InvalidInput(
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
                if (account.patientId != session.patientId?.toLong()) error("Cuenta incorrecta")
                val response = accounts.updateContact(
                    account.id ?: error("No hay cuenta"),
                    UpdateContactRequest(account.email ?: error("No hay correo"), phone),
                )
                if (!response.isSuccessful) error("No se pudo guardar el teléfono")
                _uiState.value = ChangePhoneUiState.Saved
            } catch (_: Exception) {
                _uiState.value = ChangePhoneUiState.Error(R.string.error_generic)
            }
        }
    }

    fun dismissInvalidInput() {
        val current = _uiState.value
        if (current is ChangePhoneUiState.InvalidInput) {
            _uiState.value = ChangePhoneUiState.Editing()
        }
    }
    fun retry() { _uiState.value = ChangePhoneUiState.Editing() }
}
