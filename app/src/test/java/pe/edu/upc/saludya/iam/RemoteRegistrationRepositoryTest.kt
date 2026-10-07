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
import pe.edu.upc.saludya.iam.data.repository.RemoteRegistrationRepository
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RemoteRegistrationRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: RemoteRegistrationRepository
    private val gson = Gson()
    private val identity = VerifiedIdentity(IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1)))
    private val credentials = RegistrationCredentials("lucia@example.com", "SaludYa123", "987654321")

    @Before fun setup() {
        server = MockWebServer()
        server.start()
        val api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(RegistrationApi::class.java)
        repository = RemoteRegistrationRepository(api, gson)
    }
    @After fun teardown() { server.shutdown() }

    @Test fun `registration sends verified identity and credentials with backend field names`() = runTest {
        server.enqueue(MockResponse().setResponseCode(201).setBody("{\"id\":1,\"userId\":2,\"dni\":\"01234567\"}"))
        assertEquals(RegistrationResult.Accepted(RegistrationReceipt(1, 2, credentials.email)), repository.register(identity, credentials))
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("POST", request.method)
        assertEquals("/api/v1/user-accounts", request.path)
        val json = gson.fromJson(request.body.readUtf8(), com.google.gson.JsonObject::class.java)
        assertEquals("01234567", json["dni"].asString)
        assertEquals("Lucía", json["name"].asString)
        assertEquals("Torres", json["lastname"].asString)
        assertEquals("2000-05-01", json["birthDate"].asString)
        assertEquals(credentials.email, json["email"].asString)
        assertEquals(credentials.phone, json["phone"].asString)
        assertEquals(credentials.password, json["password"].asString)
        assertFalse(json.has("confirmPassword"))
        assertNull(request.getHeader("Authorization"))
    }

    @Test fun `email conflict is different from duplicate DNI`() = runTest {
        server.enqueue(MockResponse().setResponseCode(409).setBody("{\"code\":\"IAM_EMAIL_ALREADY_REGISTERED\"}"))
        assertEquals(RegistrationResult.EmailInUse, repository.register(identity, credentials))
        server.enqueue(MockResponse().setResponseCode(409).setBody("{\"code\":\"IAM_DNI_ALREADY_REGISTERED\"}"))
        assertEquals(RegistrationResult.IdentityAlreadyRegistered, repository.register(identity, credentials))
    }

    @Test fun `generic duplicate does not falsely identify the email`() = runTest {
        server.enqueue(MockResponse().setResponseCode(409).setBody("{\"code\":\"IAM_DUPLICATE_RECORD\"}"))
        assertEquals(RegistrationResult.DuplicateRecord, repository.register(identity, credentials))
    }

    @Test fun `identity mismatch and adult restriction remain distinct`() = runTest {
        server.enqueue(MockResponse().setResponseCode(422).setBody("{\"code\":\"IAM_IDENTITY_MISMATCH\"}"))
        assertEquals(RegistrationResult.IdentityMismatch, repository.register(identity, credentials))
        server.enqueue(MockResponse().setResponseCode(422).setBody("{\"code\":\"IAM_ADULT_REQUIRED\"}"))
        assertEquals(RegistrationResult.AdultRequired, repository.register(identity, credentials))
    }

    @Test fun `bad request and outage are reported correctly`() = runTest {
        server.enqueue(MockResponse().setResponseCode(400))
        assertEquals(RegistrationResult.InvalidRequest, repository.register(identity, credentials))
        server.enqueue(MockResponse().setResponseCode(503))
        assertEquals(RegistrationResult.Unavailable, repository.register(identity, credentials))
    }

    @Test fun `incomplete response or wrong DNI cannot be accepted`() = runTest {
        for (body in listOf("{}", "{\"id\":1,\"userId\":2,\"dni\":\"87654321\"}", "{\"id\":0,\"userId\":2,\"dni\":\"01234567\"}")) {
            server.enqueue(MockResponse().setResponseCode(201).setBody(body))
            assertEquals(RegistrationResult.UnexpectedError, repository.register(identity, credentials))
        }
    }

    @Test fun `connection error is not registration success`() = runTest {
        val api = object : RegistrationApi {
            override suspend fun register(request: RegisterPatientRequest): Response<RegisterPatientResponse> = throw IOException("offline")
        }
        assertEquals(RegistrationResult.Unavailable, RemoteRegistrationRepository(api, gson).register(identity, credentials))
    }

    @Test fun `cancellation propagates`() = runTest {
        val api = object : RegistrationApi {
            override suspend fun register(request: RegisterPatientRequest): Response<RegisterPatientResponse> = throw CancellationException()
        }
        try {
            RemoteRegistrationRepository(api, gson).register(identity, credentials)
            fail("Cancellation must propagate")
        } catch (_: CancellationException) { }
    }
}
