package pe.edu.upc.saludya.features.configuration.presentation.verifyIdentity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.configuration.domain.repository.LinkedMinorRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import javax.inject.Inject

@HiltViewModel
class VerifyIdentityViewModel @Inject constructor(
    private val linkedMinors: LinkedMinorRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<VerifyIdentityUiState>(VerifyIdentityUiState.Editing())
    val uiState: StateFlow<VerifyIdentityUiState> = _uiState.asStateFlow()

    private val dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    fun onMinorNameChange(newValue: String) {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.Editing) {
            _uiState.value = current.copy(minorName = newValue)
        }
    }

    fun onMinorLastnameChange(newValue: String) {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.Editing) {
            _uiState.value = current.copy(minorLastname = newValue)
        }
    }

    fun onBirthDateChange(newValue: String) {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.Editing) {
            _uiState.value = current.copy(birthDate = newValue)
        }
    }

    fun submit(dni: String) {
        val current = _uiState.value
        if (current !is VerifyIdentityUiState.Editing) return

        val name = current.minorName.trim()
        val lastname = current.minorLastname.trim()
        val birthDate = parseDate(current.birthDate.trim())

        if (name.isEmpty() || lastname.isEmpty() || birthDate == null) {
            _uiState.value = VerifyIdentityUiState.ValidationFailed(
                R.string.error_validation_failed
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isSubmitting = true)
            try {
                val linkId = linkedMinors.linkMinor(
                    dni = dni,
                    name = name,
                    lastname = lastname,
                    birthDate = birthDate.toString(),
                )
                _uiState.value = VerifyIdentityUiState.Success(linkId)
            } catch (e: Exception) {
                _uiState.value = VerifyIdentityUiState.Failure(
                    e.message ?: "No se pudo vincular al menor."
                )
            }
        }
    }

    private fun parseDate(value: String): LocalDate? {
        if (value.isBlank()) return null
        return runCatching { LocalDate.parse(value, dateFormat) }
            .recoverCatching { LocalDate.parse(value) }
            .getOrNull()
            ?.takeIf { it.isBefore(LocalDate.now()) }
    }

    fun dismissValidationFailed() {
        val current = _uiState.value
        if (current is VerifyIdentityUiState.ValidationFailed) {
            _uiState.value = VerifyIdentityUiState.Editing(
                minorName = "",
                minorLastname = "",
                birthDate = "",
            )
        }
    }

    fun dismissFailure() {
        if (_uiState.value is VerifyIdentityUiState.Failure) {
            _uiState.value = VerifyIdentityUiState.Editing()
        }
    }
}
