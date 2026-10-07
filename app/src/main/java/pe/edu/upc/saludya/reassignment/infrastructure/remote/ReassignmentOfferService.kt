package pe.edu.upc.saludya.reassignment.infrastructure.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReassignmentOfferService {

    /** GET /reassignment-offers/pending -> JSON array of offers. */
    @GET("reassignment-offers/pending")
    suspend fun getPending(
        @Query("appointmentId") appointmentId: Long? = null
    ): Response<List<ReassignmentOfferDto>>

    @POST("reassignment-offers/{id}/accept")
    suspend fun accept(@Path("id") id: Long): Response<ReassignmentOfferDto>

    @POST("reassignment-offers/{id}/reject")
    suspend fun reject(@Path("id") id: Long): Response<ReassignmentOfferDto>
}
