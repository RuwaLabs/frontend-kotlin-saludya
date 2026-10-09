package pe.edu.upc.saludya.checkin.application.usecase

import pe.edu.upc.saludya.checkin.domain.entity.QrCodeInfo
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository

/** Historia "Código QR": obtiene el QR personal de una cita. */
class GetQrCodeUseCase(
    private val repository: CheckInRepository,
) {
    suspend operator fun invoke(appointmentId: Int): QrCodeInfo =
        repository.getQrCode(appointmentId)
}
