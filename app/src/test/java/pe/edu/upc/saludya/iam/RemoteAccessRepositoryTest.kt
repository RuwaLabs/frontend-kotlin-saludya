package pe.edu.upc.saludya.iam

import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.iam.data.remote.*
import pe.edu.upc.saludya.iam.data.repository.RemoteAccessRepository
import pe.edu.upc.saludya.iam.domain.repository.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

class RemoteAccessRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: RemoteAccessRepository
    private lateinit var api: AccessApi
    private val clock = Clock.fixed(Instant.parse("2026-10-07T12:00:00Z"), ZoneOffset.UTC)
    @Before fun setup() {
        server = MockWebServer(); server.start()
        api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build().create(AccessApi::class.java)
        repository = RemoteAccessRepository(api, Gson(), clock)
    }
    @After fun teardown() { server.shutdown() }
    private fun enqueue(status: Int, body: String = "") { server.enqueue(MockResponse().setResponseCode(status).setBody(body)) }
    private fun failure(result: AccessResult<*>, expected: AccessFailure) { assertEquals(AccessResult.Failure(expected), result) }
    private val validSession = """{"accessToken":"signed-token","tokenType":"Bearer","expiresAt":"2026-10-07T13:00:00Z","userId":2,"role":"PATIENT","patientId":1}"""

    @Test fun `password login returns a challenge rather than granting a session`() = runTest {
        enqueue(200, """{"challengeId":"challenge","maskedEmail":"l***@example.com","expiresAt":"2026-10-07T12:15:00Z"}""")
        val result = repository.login("lucia@example.com", " secret ")
        assertTrue(result is AccessResult.Success)
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/user-accounts/login", request.path)
        assertEquals("POST", request.method)
        val json = Gson().fromJson(request.body.readUtf8(), com.google.gson.JsonObject::class.java)
        assertEquals(" secret ", json["password"].asString)
        assertNull(request.getHeader("Authorization"))
    }
    @Test fun `unregistered email and wrong password receive the same failure`() = runTest {
        repeat(2) {
            enqueue(401, """{"code":"IAM_INVALID_CREDENTIALS"}""")
            failure(repository.login("lucia@example.com", "wrong"), AccessFailure.INVALID_CREDENTIALS)
        }
    }
    @Test fun `explicit inactive account response maps to admission message`() = runTest {
        enqueue(403, """{"code":"IAM_ACCOUNT_INACTIVE"}""")
        failure(repository.login("lucia@example.com", "valid"), AccessFailure.INACTIVE_ACCOUNT)
    }
    @Test fun `incomplete malformed or expired challenge never grants access`() = runTest {
        for (body in listOf("{}", "not-json", """{"challengeId":"x","maskedEmail":"l***@example.com","expiresAt":"2026-10-07T11:00:00Z"}""")) {
            enqueue(200, body)
            failure(repository.login("lucia@example.com", "valid"), AccessFailure.UNEXPECTED)
        }
    }
    @Test fun `verify login sends the challenge and six digit code and accepts patient token`() = runTest {
        enqueue(200, validSession)
        val result = repository.verifyLogin("challenge", "123456") as AccessResult.Success
        assertEquals(1L, result.value.patientId)
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/user-accounts/login/verify", request.path)
        assertEquals("""{"challengeId":"challenge","code":"123456"}""", request.body.readUtf8())
        assertFalse(result.value.toString().contains("signed-token"))
    }
    @Test fun `staff accounts cannot open the patient application`() = runTest {
        enqueue(200, validSession.replace("PATIENT", "ADMISSION_STAFF"))
        failure(repository.verifyLogin("challenge", "123456"), AccessFailure.NOT_PATIENT)
    }
    @Test fun `incomplete expired or unsafe session responses are rejected`() = runTest {
        for (body in listOf("{}", validSession.replace("signed-token", ""),
            validSession.replace("signed-token", "unsafe token"), validSession.replace("13:00:00", "11:00:00"),
            validSession.replace("\"patientId\":1", "\"patientId\":null"), validSession.replace("Bearer", "Basic"))) {
            enqueue(200, body)
            failure(repository.verifyLogin("challenge", "123456"), AccessFailure.UNEXPECTED)
        }
    }
    @Test fun `code failure expiry and rate limit remain distinct`() = runTest {
        for ((status, code, expected) in listOf(Triple(422, "IAM_INVALID_EMAIL_CODE", AccessFailure.INVALID_CODE),
            Triple(422, "IAM_LOGIN_CHALLENGE_EXPIRED", AccessFailure.EXPIRED_CODE),
            Triple(429, "IAM_EMAIL_TOO_MANY_ATTEMPTS", AccessFailure.TOO_MANY_ATTEMPTS))) {
            enqueue(status, """{"code":"$code"}""")
            failure(repository.verifyLogin("challenge", "123456"), expected)
        }
    }
    @Test fun `resend uses the original challenge and accepts only server acknowledgment`() = runTest {
        enqueue(202)
        assertEquals(AccessResult.Success(Unit), repository.resendLogin("challenge"))
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/user-accounts/login/resend", request.path)
        assertEquals("""{"challengeId":"challenge"}""", request.body.readUtf8())
        enqueue(503); failure(repository.resendLogin("challenge"), AccessFailure.UNAVAILABLE)
    }
    @Test fun `recovery success and unknown address share the same generic result`() = runTest {
        for (status in listOf(202, 404)) {
            enqueue(status)
            assertEquals(AccessResult.Success(Unit), repository.recoverPassword("lucia@example.com"))
            val request = server.takeRequest(1, TimeUnit.SECONDS)!!
            assertEquals("/api/v1/user-accounts/recover-password", request.path)
            assertEquals("""{"email":"lucia@example.com"}""", request.body.readUtf8())
        }
    }
    @Test fun `recovery outage does not pretend a link was sent`() = runTest {
        enqueue(503); failure(repository.recoverPassword("lucia@example.com"), AccessFailure.UNAVAILABLE)
        enqueue(429); failure(repository.recoverPassword("lucia@example.com"), AccessFailure.TOO_MANY_ATTEMPTS)
    }
    @Test fun `logout submits bearer token only to revocation endpoint`() = runTest {
        enqueue(204); assertEquals(AccessResult.Success(Unit), repository.logout("signed-token"))
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("/api/v1/user-accounts/logout", request.path)
        assertEquals("Bearer signed-token", request.getHeader("Authorization"))
    }
    @Test fun `transport error is unavailable and cancellation propagates`() = runTest {
        val offline = object : AccessApi by api {
            override suspend fun recoverPassword(request: EmailRequest): retrofit2.Response<Void> = throw java.io.IOException("offline")
        }
        failure(RemoteAccessRepository(offline, Gson(), clock).recoverPassword("lucia@example.com"), AccessFailure.UNAVAILABLE)
        val cancelled = object : AccessApi by api {
            override suspend fun recoverPassword(request: EmailRequest): retrofit2.Response<Void> = throw CancellationException()
        }
        try {
            RemoteAccessRepository(cancelled, Gson(), clock).recoverPassword("lucia@example.com"); fail()
        } catch (_: CancellationException) { }
    }
}
