package pe.edu.upc.saludya.features.checkin.application.usecase

import javax.inject.Inject

import pe.edu.upc.saludya.features.checkin.domain.model.QrCodeInfo
import pe.edu.upc.saludya.features.checkin.domain.repositories.CheckInRepository

/** Historia "Código QR": obtiene el QR personal de una cita. */
class GetQrCodeUseCase @Inject constructor(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int): QrCodeInfo =
        repository.getQrCode(appointmentId)
}