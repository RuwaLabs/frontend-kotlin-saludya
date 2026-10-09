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
import pe.edu.upc.saludya.iam.data.repository.RemoteIdentityRepository
import pe.edu.upc.saludya.iam.domain.model.IdentityDetails
import pe.edu.upc.saludya.iam.domain.repository.IdentityVerificationResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class RemoteIdentityRepositoryTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: RemoteIdentityRepository
    private val gson = Gson()
    private val details = IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1))

    @Before fun setup() {
        server = MockWebServer()
        server.start()
        val api = Retrofit.Builder().baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(IdentityApi::class.java)
        repository = RemoteIdentityRepository(api, gson)
    }
    @After fun teardown() { server.shutdown() }

    @Test fun `request matches backend contract without external provider credentials`() = runTest {
        server.enqueue(MockResponse().setHeader("Content-Type", "application/json").setBody("{\"verified\":true}"))
        val result = repository.verify(details)
        assertTrue(result is IdentityVerificationResult.Verified)
        val request = server.takeRequest(1, TimeUnit.SECONDS)!!
        assertEquals("POST", request.method)
        assertEquals("/api/v1/identity-verifications", request.path)
        assertNull(request.getHeader("Authorization"))
        val body = gson.fromJson(request.body.readUtf8(), VerifyIdentityRequest::class.java)
        assertEquals("01234567", body.dni)
        assertEquals("Lucía", body.name)
        assertEquals("Torres", body.lastname)
        assertEquals("2000-05-01", body.birthDate)
    }

    @Test fun `identity mismatch maps to a correctable rejection`() = runTest {
        server.enqueue(MockResponse().setResponseCode(422).setBody("{\"code\":\"IAM_IDENTITY_MISMATCH\"}"))
        assertEquals(IdentityVerificationResult.Mismatch, repository.verify(details))
    }

    @Test fun `provider outage maps to retryable unavailability`() = runTest {
        server.enqueue(MockResponse().setResponseCode(503).setBody("{\"code\":\"IAM_IDENTITY_UNAVAILABLE\"}"))
        assertEquals(IdentityVerificationResult.Unavailable, repository.verify(details))
    }

    @Test fun `false verification cannot grant access`() = runTest {
        server.enqueue(MockResponse().setBody("{\"verified\":false}"))
        assertEquals(IdentityVerificationResult.Mismatch, repository.verify(details))
    }

    @Test fun `empty or malformed successful responses fail closed`() = runTest {
        for (body in listOf("{}", "{\"verified\":null}", "not json")) {
            server.enqueue(MockResponse().setBody(body))
            assertEquals(IdentityVerificationResult.UnexpectedError, repository.verify(details))
        }
    }

    @Test fun `unrelated 422 is not presented as an identity mismatch`() = runTest {
        server.enqueue(MockResponse().setResponseCode(422).setBody("{\"code\":\"IAM_ADULT_REQUIRED\"}"))
        assertEquals(IdentityVerificationResult.UnexpectedError, repository.verify(details))
    }

    @Test fun `invalid request and server errors remain distinct`() = runTest {
        server.enqueue(MockResponse().setResponseCode(400))
        assertEquals(IdentityVerificationResult.InvalidRequest, repository.verify(details))
        server.enqueue(MockResponse().setResponseCode(502))
        assertEquals(IdentityVerificationResult.Unavailable, repository.verify(details))
    }

    @Test fun `connection failure does not verify identity`() = runTest {
        val brokenApi = object : IdentityApi {
            override suspend fun verify(request: VerifyIdentityRequest): Response<VerifyIdentityResponse> = throw IOException("offline")
        }
        assertEquals(IdentityVerificationResult.Unavailable, RemoteIdentityRepository(brokenApi, gson).verify(details))
    }

    @Test fun `cancellation propagates instead of showing a network error`() = runTest {
        val cancelledApi = object : IdentityApi {
            override suspend fun verify(request: VerifyIdentityRequest): Response<VerifyIdentityResponse> = throw CancellationException()
        }
        try {
            RemoteIdentityRepository(cancelledApi, gson).verify(details)
            fail("Cancellation must propagate")
        } catch (_: CancellationException) { }
    }
}
