package pe.edu.upc.saludya.iam.presentation.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.iam.domain.model.AccessValidation
import pe.edu.upc.saludya.iam.domain.repository.*
import javax.inject.Inject

data class RecoveryHelpUiState(
    val dni: String = "", val email: String = "", val invalidDni: Boolean = false,
    val invalidEmail: Boolean = false, val busy: Boolean = false,
    val receipt: RecoveryHelpReceipt? = null, val failure: AccessFailure? = null
)

@HiltViewModel
class RecoveryHelpViewModel @Inject constructor(private val repository: RecoveryActionsRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(RecoveryHelpUiState())
    val state = mutableState.asStateFlow()
    fun editDni(value: String) {
        val current = mutableState.value
        if (!current.busy && current.receipt == null) mutableState.value = current.copy(dni = value, invalidDni = false, failure = null)
    }
    fun editEmail(value: String) {
        val current = mutableState.value
        if (!current.busy && current.receipt == null) mutableState.value = current.copy(email = value, invalidEmail = false, failure = null)
    }
    fun reset() { if (!mutableState.value.busy) mutableState.value = RecoveryHelpUiState() }
    fun submit() {
        val current = mutableState.value
        if (current.busy || current.receipt != null) return
        val invalidDni = !current.dni.matches(Regex("[0-9]{8}"))
        val email = AccessValidation.email(current.email)
        if (invalidDni || email == null) {
            mutableState.value = current.copy(invalidDni = invalidDni, invalidEmail = email == null)
            return
        }
        mutableState.value = current.copy(email = email, busy = true, failure = null)
        viewModelScope.launch {
            mutableState.value = when (val result = repository.requestHelp(current.dni, email)) {
                is AccessResult.Success -> mutableState.value.copy(busy = false, receipt = result.value)
                is AccessResult.Failure -> mutableState.value.copy(busy = false, failure = result.reason)
            }
        }
    }
}
