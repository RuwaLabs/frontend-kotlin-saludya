package pe.edu.upc.saludya.features.checkin.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class CheckInTicketDto(
    val id: Int,
    val appointmentId: Int,
    val bookingCode: String?,
    val turnCode: String?,
    val status: String,
    val checkedInAt: String,
)

data class QueuePositionDto(val position: Int, val totalInQueue: Int)

data class QrTokenDto(
    val qrToken: String,
)

interface CheckInApi {
    @GET("check-ins/appointment/{appointmentId}")
    suspend fun getTicket(@Path("appointmentId") appointmentId: Int): Response<CheckInTicketDto>

    @GET("check-ins/appointment/{appointmentId}/position")
    suspend fun getPosition(@Path("appointmentId") appointmentId: Int): Response<QueuePositionDto>

    @GET("check-ins/appointment/{appointmentId}/qr-token")
    suspend fun getQrToken(
        @Path("appointmentId") appointmentId: Int,
    ): Response<QrTokenDto>
}
