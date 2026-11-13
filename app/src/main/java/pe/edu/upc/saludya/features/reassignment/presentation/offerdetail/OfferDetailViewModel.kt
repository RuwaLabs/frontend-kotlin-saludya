package pe.edu.upc.saludya.features.reassignment.presentation.offerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upc.saludya.features.reassignment.application.AcceptOfferByIdUseCase
import pe.edu.upc.saludya.features.reassignment.application.RejectOfferByIdUseCase
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    private val acceptOffer: AcceptOfferByIdUseCase,
    private val rejectOffer: RejectOfferByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OfferDetailUiState())
    val uiState: StateFlow<OfferDetailUiState> = _uiState.asStateFlow()

    fun setOfferId(id: Long) {
        _uiState.update { it.copy(offerId = id) }
    }

    fun accept() {
        val offerId = _uiState.value.offerId
        if (offerId <= 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = acceptOffer(offerId)
            _uiState.update { it.copy(isLoading = false, isAccepted = result != null) }
        }
    }

    fun reject() {
        val offerId = _uiState.value.offerId
        if (offerId <= 0) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = rejectOffer(offerId)
            _uiState.update { it.copy(isLoading = false, isRejected = result != null) }
        }
    }
}
