package pe.edu.upc.saludya.reassignment.presentation.offerdetail

data class OfferDetailUiState(
    val offerId: Long = -1L,
    val isLoading: Boolean = false,
    val isAccepted: Boolean = false,
    val isRejected: Boolean = false,
    val errorMessage: String? = null
)
