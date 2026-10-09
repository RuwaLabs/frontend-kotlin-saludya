package pe.edu.upc.saludya.features.iam

import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.features.iam.infrastructure.remote.*
import pe.edu.upc.saludya.features.iam.infrastructure.repositories.RemoteRecoveryActionsRepository
import pe.edu.upc.saludya.features.iam.domain.repository.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RecoveryActionsRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var api: RecoveryActionsApi
    private lateinit var repository: RemoteRecoveryActionsRepository
    @Before fun setup() {
        server = MockWebServer(); server.start()
        api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build().create(RecoveryActionsApi::class.java)
        repository = RemoteRecoveryActionsRepository(api, Gson())
    }
    @After fun teardown() { server.shutdown() }
    @Test fun `reset sends link token password and confirmation without authentication`() = runTest {
        server.enqueue(MockResponse().setResponseCode(204))
        assertEquals(AccessResult.Success(Unit), repository.resetPassword("token", " New12345 ", " New12345 "))
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/user-accounts/reset-password", request.path)
        assertEquals("POST", request.method)
        assertNull(request.getHeader("Authorization"))
        val body = Gson().fromJson(request.body.readUtf8(), com.google.gson.JsonObject::class.java)
        assertEquals("token", body["token"].asString)
        assertEquals(" New12345 ", body["password"].asString)
        assertEquals(body["password"], body["confirmPassword"])
    }
    @Test fun `expired or consumed recovery link is not success`() = runTest {
        server.enqueue(MockResponse().setResponseCode(422).setBody("""{"code":"IAM_RESET_TOKEN_INVALID"}"""))
        assertEquals(AccessResult.Failure(AccessFailure.RESET_LINK_INVALID), repository.resetPassword("token", "New12345", "New12345"))
    }
    @Test fun `server mismatch maps to confirmation error`() = runTest {
        server.enqueue(MockResponse().setResponseCode(422).setBody("""{"code":"IAM_PASSWORD_MISMATCH"}"""))
        assertEquals(AccessResult.Failure(AccessFailure.PASSWORD_MISMATCH), repository.resetPassword("token", "New12345", "Other123"))
    }
    @Test fun `unexpected successful HTTP status cannot claim password update`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200))
        assertEquals(AccessResult.Failure(AccessFailure.UNEXPECTED), repository.resetPassword("token", "New12345", "New12345"))
    }
    @Test fun `assisted recovery posts DNI and alternate email and returns instructions`() = runTest {
        server.enqueue(MockResponse().setResponseCode(202).setBody("""{"instructions":"Acude con tu DNI","phone":"999888777"}"""))
        assertEquals(AccessResult.Success(RecoveryHelpReceipt("Acude con tu DNI", "999888777")),
            repository.requestHelp("01234567", "contacto@example.com"))
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/account-recovery-requests", request.path)
        assertNull(request.getHeader("Authorization"))
        assertEquals("""{"dni":"01234567","contactEmail":"contacto@example.com"}""", request.body.readUtf8())
    }
    @Test fun `outage or malformed support response does not register a request`() = runTest {
        server.enqueue(MockResponse().setResponseCode(503))
        assertEquals(AccessResult.Failure(AccessFailure.UNAVAILABLE), repository.requestHelp("01234567", "contacto@example.com"))
        server.enqueue(MockResponse().setResponseCode(202).setBody("invalid-json"))
        assertEquals(AccessResult.Failure(AccessFailure.UNEXPECTED), repository.requestHelp("01234567", "contacto@example.com"))
    }
    @Test fun `network failures are retryable and cancellation propagates`() = runTest {
        val offline = object : RecoveryActionsApi by api {
            override suspend fun resetPassword(request: ResetPasswordRequest): retrofit2.Response<Void> = throw IOException()
        }
        assertEquals(AccessResult.Failure(AccessFailure.UNAVAILABLE),
            RemoteRecoveryActionsRepository(offline, Gson()).resetPassword("token", "New12345", "New12345"))
        val cancelled = object : RecoveryActionsApi by api {
            override suspend fun requestHelp(request: RecoveryHelpRequest): retrofit2.Response<RecoveryHelpResponse> = throw CancellationException()
        }
        try { RemoteRecoveryActionsRepository(cancelled, Gson()).requestHelp("01234567", "contacto@example.com"); fail() }
        catch (_: CancellationException) { }
    }
    @Test fun `reset request never prints token or passwords`() {
        val request = ResetPasswordRequest("secret-token", "secret-password", "secret-password")
        assertFalse(request.toString().contains("secret"))
    }
}
