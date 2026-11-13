package pe.edu.upc.saludya.features.patientreservations.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import pe.edu.upc.saludya.features.patientreservations.presentation.beneficiario.BeneficiarioScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.bloqueHoras.BloqueHorasScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.busquedaSinResultados.BusquedaSinResultadosScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.elegirFecha.ElegirFechaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.elegirProfesional.ElegirProfesionalScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.reservaConfirmada.ReservaConfirmadaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.reservaTuCita.ReservaTuCitaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.resumenReserva.ResumenReservaScreen
import pe.edu.upc.saludya.features.patientreservations.presentation.sinCuposDia.SinCuposDiaScreen

@Serializable
data object PatientReservationsNavGraphRoute

@Serializable
data object ReservaRoute

@Serializable
data class BeneficiarioRoute(val specialityId: Int)

@Serializable
data class FechaRoute(val specialityId: Int, val beneficiaryId: Int)

@Serializable
data class ProfesionalRoute(
    val specialityId: Int,
    val beneficiaryId: Int,
    val date: String
)

@Serializable
data class HoraProfesionalRoute(
    val specialityId: Int,
    val beneficiaryId: Int,
    val date: String,
    val doctorId: Int
)

@Serializable
data class HoraEspecialidadRoute(
    val specialityId: Int,
    val beneficiaryId: Int,
    val date: String
)

@Serializable
data class ResumenRoute(
    val specialityId: Int,
    val beneficiaryId: Int,
    val timeSlotId: Int
)

@Serializable
data class ConfirmadaRoute(val appointmentId: Int)

@Serializable
data class SinCuposRoute(
    val specialityId: Int,
    val beneficiaryId: Int,
    val date: String
)

@Serializable
data class SinResultadosRoute(val query: String)

fun NavGraphBuilder.patientReservationsNavGraph(navController: NavController) {

    navigation<PatientReservationsNavGraphRoute>(startDestination = ReservaRoute) {

        // R01 · Reserva tu cita
        composable<ReservaRoute> {
            ReservaTuCitaScreen(
                onSpecialityClick = { specialityId ->
                    navController.navigate(BeneficiarioRoute(specialityId))
                },
                onNoResults = { query ->
                    navController.navigate(SinResultadosRoute(query))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R02 · ¿Para quién es la cita?
        composable<BeneficiarioRoute> { entry ->
            val route = entry.toRoute<BeneficiarioRoute>()
            BeneficiarioScreen(
                onContinue = { beneficiaryId ->
                    navController.navigate(FechaRoute(route.specialityId, beneficiaryId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R03 · Elige la fecha
        composable<FechaRoute> { entry ->
            val route = entry.toRoute<FechaRoute>()
            ElegirFechaScreen(
                specialityId = route.specialityId,
                beneficiaryId = route.beneficiaryId,
                onContinue = { date ->
                    navController.navigate(
                        ProfesionalRoute(route.specialityId, route.beneficiaryId, date)
                    )
                },
                onNoSlots = { date ->
                    navController.navigate(
                        SinCuposRoute(route.specialityId, route.beneficiaryId, date)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R04 · Elige al profesional
        composable<ProfesionalRoute> { entry ->
            val route = entry.toRoute<ProfesionalRoute>()
            ElegirProfesionalScreen(
                specialityId = route.specialityId,
                beneficiaryId = route.beneficiaryId,
                onProfessionalClick = { doctorId ->
                    navController.navigate(
                        HoraProfesionalRoute(
                            route.specialityId, route.beneficiaryId, route.date, doctorId
                        )
                    )
                },
                onChooseByTime = {
                    navController.navigate(
                        HoraEspecialidadRoute(route.specialityId, route.beneficiaryId, route.date)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R05 · Elige la hora
        composable<HoraProfesionalRoute> { entry ->
            val route = entry.toRoute<HoraProfesionalRoute>()
            BloqueHorasScreen(
                specialityId = route.specialityId,
                beneficiaryId = route.beneficiaryId,
                date = route.date,
                doctorId = route.doctorId,
                onContinue = { timeSlotId ->
                    navController.navigate(
                        ResumenRoute(route.specialityId, route.beneficiaryId, timeSlotId)
                    )
                },
                onNoSlots = {
                    navController.navigate(
                        SinCuposRoute(route.specialityId, route.beneficiaryId, route.date)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R06 · Horarios disponibles
        composable<HoraEspecialidadRoute> { entry ->
            val route = entry.toRoute<HoraEspecialidadRoute>()
            BloqueHorasScreen(
                specialityId = route.specialityId,
                beneficiaryId = route.beneficiaryId,
                date = route.date,
                doctorId = null,
                onContinue = { timeSlotId ->
                    navController.navigate(
                        ResumenRoute(route.specialityId, route.beneficiaryId, timeSlotId)
                    )
                },
                onNoSlots = {
                    navController.navigate(
                        SinCuposRoute(route.specialityId, route.beneficiaryId, route.date)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R07 · Confirma tu cita
        composable<ResumenRoute> { entry ->
            val route = entry.toRoute<ResumenRoute>()
            ResumenReservaScreen(
                specialityId = route.specialityId,
                beneficiaryId = route.beneficiaryId,
                timeSlotId = route.timeSlotId,
                onBooked = { appointmentId ->
                    navController.navigate(ConfirmadaRoute(appointmentId)) {
                        popUpTo(PatientReservationsNavGraphRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R08 · Reserva confirmada
        composable<ConfirmadaRoute> { entry ->
            val route = entry.toRoute<ConfirmadaRoute>()
            ReservaConfirmadaScreen(
                appointmentId = route.appointmentId,
                onViewAppointments = {
                    navController.navigate(ReservaRoute) {
                        popUpTo(PatientReservationsNavGraphRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBack = {
                    navController.navigate(ReservaRoute) {
                        popUpTo(PatientReservationsNavGraphRoute) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        // R09 · Sin horarios disponibles
        composable<SinCuposRoute> { entry ->
            val route = entry.toRoute<SinCuposRoute>()
            SinCuposDiaScreen(
                specialityId = route.specialityId,
                date = route.date,
                onChooseAnotherDate = {
                    navController.navigate(FechaRoute(route.specialityId, route.beneficiaryId)) {
                        launchSingleTop = true
                    }
                },
                onViewNextAvailability = { nextDate ->
                    navController.navigate(
                        HoraEspecialidadRoute(route.specialityId, route.beneficiaryId, nextDate)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }

        // R10 · Búsqueda sin resultados
        composable<SinResultadosRoute> { entry ->
            val route = entry.toRoute<SinResultadosRoute>()
            BusquedaSinResultadosScreen(
                initialQuery = route.query,
                onClearSearch = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
