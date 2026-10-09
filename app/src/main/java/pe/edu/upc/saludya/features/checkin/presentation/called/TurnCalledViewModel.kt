package pe.edu.upc.saludya.features.checkin.presentation.called

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pe.edu.upc.saludya.features.checkin.application.usecase.ObserveTurnCallUseCase
import javax.inject.Inject

@HiltViewModel
class TurnCalledViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeTurnCall: ObserveTurnCallUseCase,
) : ViewModel() {

    private val appointmentId: Int = checkNotNull(savedStateHandle["appointmentId"])

    val uiState: StateFlow<TurnCalledUiState> = observeTurnCall(appointmentId)
        .map { call ->
            TurnCalledUiState(
                ticket = call.ticket,
                postCallToleranceMinutes = call.postCallToleranceMinutes,
            )
        }
        .catch { emit(TurnCalledUiState(errorMessage = it.message ?: "No se pudo cargar tu turno.")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TurnCalledUiState(isLoading = true))
}
