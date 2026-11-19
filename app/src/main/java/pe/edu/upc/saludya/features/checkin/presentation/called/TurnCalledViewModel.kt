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
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import pe.edu.upc.saludya.R
import pe.edu.upc.saludya.features.checkin.application.usecase.ObserveTurnCallUseCase
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository
import javax.inject.Inject

@HiltViewModel
class TurnCalledViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    observeTurnCall: ObserveTurnCallUseCase,
    private val repository: CheckInRepository,
) : ViewModel() {

    private val appointmentId: Int = checkNotNull(savedStateHandle["appointmentId"])

    val uiState: StateFlow<TurnCalledUiState> = observeTurnCall(appointmentId)
        .map { call ->
            TurnCalledUiState(
                ticket = call.ticket,
                postCallToleranceMinutes = call.postCallToleranceMinutes,
                isAttended = call.ticket?.isAttended == true,
            )
        }
        .catch {
            emit(
                TurnCalledUiState(
                    errorMessage = it.message
                        ?: context.getString(R.string.checkin_turn_load_error),
                ),
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TurnCalledUiState(isLoading = true))

    fun refresh() = repository.refresh(appointmentId)
}
