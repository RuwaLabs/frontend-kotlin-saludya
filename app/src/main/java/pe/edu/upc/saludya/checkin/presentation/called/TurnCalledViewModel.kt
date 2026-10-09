package pe.edu.upc.saludya.checkin.presentation.called

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pe.edu.upc.saludya.checkin.application.usecase.ObserveTurnCallUseCase
import pe.edu.upc.saludya.checkin.infrastructure.di.CheckInModule

/**
 * ViewModel de "¡Es tu turno!": ticket llamado + plazo de tolerancia del establecimiento.
 */
class TurnCalledViewModel(
    appointmentId: Int,
    observeTurnCall: ObserveTurnCallUseCase,
) : ViewModel() {

    val uiState: StateFlow<TurnCalledUiState> = observeTurnCall(appointmentId)
        .map { call ->
            TurnCalledUiState(
                ticket = call.ticket,
                postCallToleranceMinutes = call.postCallToleranceMinutes,
            )
        }
        .catch { emit(TurnCalledUiState(errorMessage = it.message ?: "No se pudo cargar tu turno.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TurnCalledUiState(isLoading = true))

    companion object {
        fun factory(appointmentId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer { TurnCalledViewModel(appointmentId, CheckInModule.observeTurnCall) }
        }
    }
}
