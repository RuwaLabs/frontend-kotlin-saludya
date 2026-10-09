package pe.edu.upc.saludya.checkin.infrastructure.di

import pe.edu.upc.saludya.checkin.application.usecase.GetCheckInReservationsUseCase
import pe.edu.upc.saludya.checkin.application.usecase.GetQrCodeUseCase
import pe.edu.upc.saludya.checkin.application.usecase.LeaveQueueUseCase
import pe.edu.upc.saludya.checkin.application.usecase.ObserveAttendanceTicketUseCase
import pe.edu.upc.saludya.checkin.application.usecase.ObserveQueueStatusUseCase
import pe.edu.upc.saludya.checkin.application.usecase.ObserveTurnCallUseCase
import pe.edu.upc.saludya.checkin.application.usecase.RegisterArrivalUseCase
import pe.edu.upc.saludya.checkin.domain.repository.CheckInRepository
import pe.edu.upc.saludya.checkin.infrastructure.repositories.InMemoryCheckInRepository

/**
 * Raíz de composición del módulo de check-in: es el único lugar que conoce la
 * implementación concreta del repositorio y arma con ella los casos de uso que
 * consumen los ViewModels.
 *
 * El repositorio es único para que las cuatro pantallas compartan el mismo estado.
 * Si se prefiere Hilt, sustituir este objeto por un `@Module` con los mismos `@Provides`.
 */
object CheckInModule {

    val repository: CheckInRepository by lazy { InMemoryCheckInRepository() }

    // Historia "Código QR"
    val getCheckInReservations by lazy { GetCheckInReservationsUseCase(repository) }
    val getQrCode by lazy { GetQrCodeUseCase(repository) }
    val registerArrival by lazy { RegisterArrivalUseCase(repository) }

    // Historia "Ticket de atención"
    val observeAttendanceTicket by lazy { ObserveAttendanceTicketUseCase(repository) }

    // Historia "Mi turno"
    val observeQueueStatus by lazy { ObserveQueueStatusUseCase(repository) }
    val leaveQueue by lazy { LeaveQueueUseCase(repository) }

    // Historia "¡Es tu turno!"
    val observeTurnCall by lazy { ObserveTurnCallUseCase(repository) }
}
