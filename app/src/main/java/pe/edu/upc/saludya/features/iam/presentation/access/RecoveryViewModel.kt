package pe.edu.upc.saludya.features.iam.presentation.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.iam.domain.model.AccessValidation
import pe.edu.upc.saludya.features.iam.domain.repository.*
import javax.inject.Inject

data class RecoveryUiState(
    val email: String = "", val busy: Boolean = false, val invalidEmail: Boolean = false,
    val requested: Boolean = false, val failure: AccessFailure? = null
)

@HiltViewModel
class RecoveryViewModel @Inject constructor(private val repository: AccessRepository) : ViewModel() {
    private val mutableState = MutableStateFlow(RecoveryUiState())
    val state = mutableState.asStateFlow()

    fun edit(value: String) {
        if (!mutableState.value.busy) mutableState.value = RecoveryUiState(email = value)
    }
    fun reset() { if (!mutableState.value.busy) mutableState.value = RecoveryUiState() }
    fun request() {
        val current = mutableState.value
        if (current.busy) return
        val email = AccessValidation.email(current.email)
        if (email == null) {
            mutableState.value = current.copy(invalidEmail = true, requested = false)
            return
        }
        mutableState.value = current.copy(email = email, busy = true, invalidEmail = false, failure = null, requested = false)
        viewModelScope.launch {
            mutableState.value = when (val result = repository.recoverPassword(email)) {
                is AccessResult.Success -> mutableState.value.copy(busy = false, requested = true)
                is AccessResult.Failure -> mutableState.value.copy(busy = false, failure = result.reason)
            }
        }
    }
}
