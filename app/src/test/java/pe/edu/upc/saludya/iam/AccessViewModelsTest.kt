package pe.edu.upc.saludya.iam

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.*
import pe.edu.upc.saludya.iam.presentation.access.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class AccessViewModelsTest {
    private val dispatcher = StandardTestDispatcher()
    private val clock = MutableClock()
    private val repository = FakeAccessRepository()
    private val sessions = PatientSessionStore(clock)
    private lateinit var login: LoginViewModel
    private lateinit var recovery: RecoveryViewModel
    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        login = LoginViewModel(repository, sessions, clock)
        recovery = RecoveryViewModel(repository)
    }
    @After fun teardown() { Dispatchers.resetMain() }
    private fun credentials() { login.editEmail(" LUCIA@example.com "); login.editPassword(" Secret123 ") }

    @Test fun `valid password only reaches challenge and clears password`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        assertNotNull(login.state.value.challenge)
        assertNull(login.state.value.session)
        assertNull(sessions.current())
        assertEquals("lucia@example.com", repository.email)
        assertEquals(" Secret123 ", repository.password)
        assertEquals("", login.state.value.password)
    }
    @Test fun `successful verification opens session and erases challenge and code`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        login.editCode("123456"); login.verify(); login.verify(); advanceUntilIdle()
        assertNotNull(sessions.current())
        assertNotNull(login.state.value.session)
        assertNull(login.state.value.challenge)
        assertEquals("", login.state.value.code)
        assertEquals(1, repository.verifications)
    }
    @Test fun `invalid credentials keep session closed and use one generic failure`() = runTest(dispatcher) {
        repository.loginResult = AccessResult.Failure(AccessFailure.INVALID_CREDENTIALS)
        credentials(); login.login(); advanceUntilIdle()
        assertEquals(AccessFailure.INVALID_CREDENTIALS, login.state.value.failure)
        assertNull(sessions.current())
        assertEquals("", login.state.value.password)
    }
    @Test fun `inactive and nonpatient accounts cannot open session`() = runTest(dispatcher) {
        repository.loginResult = AccessResult.Failure(AccessFailure.INACTIVE_ACCOUNT)
        credentials(); login.login(); advanceUntilIdle()
        assertEquals(AccessFailure.INACTIVE_ACCOUNT, login.state.value.failure)
        login.reset(); repository.loginResult = null
        credentials(); login.login(); advanceUntilIdle()
        repository.verifyResult = AccessResult.Failure(AccessFailure.NOT_PATIENT)
        login.editCode("123456"); login.verify(); advanceUntilIdle()
        assertNull(sessions.current())
    }
    @Test fun `bad email or empty password never sends credentials`() = runTest(dispatcher) {
        login.editEmail("bad"); login.editPassword("secret"); login.login(); advanceUntilIdle()
        assertEquals(LoginNotice.EMAIL_FORMAT, login.state.value.notice)
        login.editEmail("lucia@example.com"); login.editPassword(""); login.login(); advanceUntilIdle()
        assertEquals(LoginNotice.PASSWORD_REQUIRED, login.state.value.notice)
        assertEquals(0, repository.logins)
    }
    @Test fun `invalid code remains retryable but never creates a local session`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        login.editCode("abc123"); login.verify(); advanceUntilIdle()
        assertEquals(0, repository.verifications)
        repository.verifyResult = AccessResult.Failure(AccessFailure.INVALID_CODE)
        login.editCode("000000"); login.verify(); advanceUntilIdle()
        assertEquals(AccessFailure.INVALID_CODE, login.state.value.failure)
        assertNull(sessions.current())
        repository.verifyResult = null
        login.editCode("123456"); login.verify(); advanceUntilIdle()
        assertNotNull(sessions.current())
    }
    @Test fun `expired challenge cannot verify or resend and permits restarting login`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        clock.now = Instant.parse("2026-10-07T12:15:00Z")
        login.editCode("123456"); login.verify(); login.resend(); advanceUntilIdle()
        assertEquals(AccessFailure.EXPIRED_CODE, login.state.value.failure)
        assertEquals(0, repository.verifications + repository.resends)
        login.cancelChallenge()
        assertNull(login.state.value.challenge)
        assertEquals("", login.state.value.password)
    }
    @Test fun `resend uses original challenge and clears previous code`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        login.editCode("123456"); login.resend(); login.resend(); advanceUntilIdle()
        assertEquals(1, repository.resends)
        assertEquals("challenge", repository.challengeId)
        assertEquals("", login.state.value.code)
        assertEquals(LoginNotice.CODE_SENT, login.state.value.notice)
    }
    @Test fun `pending login blocks duplicate taps edits and resetting`() = runTest(dispatcher) {
        credentials(); repository.pending = CompletableDeferred()
        login.login(); login.login(); login.editEmail("other@example.com"); login.reset(); runCurrent()
        assertEquals(1, repository.logins)
        assertTrue(login.state.value.busy)
        assertEquals("lucia@example.com", login.state.value.email)
        repository.pending!!.complete(Unit); advanceUntilIdle()
    }
    @Test fun `session expiration clears token and asks for sign in`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        login.editCode("123456"); login.verify(); advanceUntilIdle()
        clock.now = Instant.parse("2026-10-07T13:00:00Z")
        login.observeExpiration()
        assertNull(sessions.current())
        assertNull(login.state.value.session)
        assertEquals(LoginNotice.SESSION_EXPIRED, login.state.value.notice)
    }
    @Test fun `offline logout still clears local session and reports unconfirmed revocation`() = runTest(dispatcher) {
        credentials(); login.login(); advanceUntilIdle()
        login.editCode("123456"); login.verify(); advanceUntilIdle()
        repository.logoutResult = AccessResult.Failure(AccessFailure.UNAVAILABLE)
        login.requestLogout(); login.logout(); assertNull(sessions.current()); advanceUntilIdle()
        assertEquals(LoginNotice.LOGOUT_UNCONFIRMED, login.state.value.notice)
        assertEquals("signed-token", repository.revokedToken)
    }
    @Test fun `recovery validates email locally and canonicalizes recipient`() = runTest(dispatcher) {
        recovery.edit("bad"); recovery.request(); advanceUntilIdle()
        assertTrue(recovery.state.value.invalidEmail)
        assertEquals(0, repository.recoveries)
        recovery.edit(" LUCIA@example.com "); recovery.request(); advanceUntilIdle()
        assertTrue(recovery.state.value.requested)
        assertEquals("lucia@example.com", repository.email)
    }
    @Test fun `new recovery request calls backend again to replace previous link`() = runTest(dispatcher) {
        recovery.edit("lucia@example.com"); recovery.request(); advanceUntilIdle()
        recovery.request(); recovery.request(); advanceUntilIdle()
        assertEquals(2, repository.recoveries)
        assertTrue(recovery.state.value.requested)
    }
    @Test fun `recovery outage remains an error rather than claiming delivery`() = runTest(dispatcher) {
        repository.recoveryResult = AccessResult.Failure(AccessFailure.UNAVAILABLE)
        recovery.edit("lucia@example.com"); recovery.request(); advanceUntilIdle()
        assertFalse(recovery.state.value.requested)
        assertEquals(AccessFailure.UNAVAILABLE, recovery.state.value.failure)
    }
    @Test fun `expired session cannot be opened or read`() {
        val expired = PatientSession("secret", clock.instant(), 2, 1)
        assertFalse(sessions.open(expired)); assertNull(sessions.current())
        val active = expired.copy(expiresAt = clock.instant().plusSeconds(10))
        assertTrue(sessions.open(active))
        clock.now = active.expiresAt
        assertNull(sessions.current())
        assertNull(sessions.session.value)
        assertFalse(active.toString().contains("secret"))
    }
    @Test fun `verification code accepts six ASCII digits including leading zero`() {
        assertTrue(AccessValidation.code("000001"))
        for (code in listOf("12345", "1234567", "１２３４５６", "12345a")) assertFalse(AccessValidation.code(code))
    }

    private class MutableClock : Clock() {
        var now: Instant = Instant.parse("2026-10-07T12:00:00Z")
        override fun instant(): Instant = now
        override fun getZone(): ZoneId = ZoneOffset.UTC
        override fun withZone(zone: ZoneId): Clock = Clock.fixed(now, zone)
    }
    private class FakeAccessRepository : AccessRepository {
        override suspend fun profile(token: String): AccessResult<PatientAccountProfile> =
            AccessResult.Success(PatientAccountProfile(2, 1, "Lucía", "Torres", "lucia@example.com"))
        var logins = 0; var verifications = 0; var resends = 0; var recoveries = 0
        var email = ""; var password = ""; var challengeId = ""; var revokedToken = ""
        var pending: CompletableDeferred<Unit>? = null
        var loginResult: AccessResult<LoginChallenge>? = null
        var verifyResult: AccessResult<PatientSession>? = null
        var recoveryResult: AccessResult<Unit> = AccessResult.Success(Unit)
        var logoutResult: AccessResult<Unit> = AccessResult.Success(Unit)
        override suspend fun login(email: String, password: String): AccessResult<LoginChallenge> {
            logins++; this.email = email; this.password = password; pending?.await()
            return loginResult ?: AccessResult.Success(LoginChallenge("challenge", "l***@example.com", Instant.parse("2026-10-07T12:15:00Z")))
        }
        override suspend fun verifyLogin(challengeId: String, code: String): AccessResult<PatientSession> {
            verifications++
            return verifyResult ?: AccessResult.Success(PatientSession("signed-token", Instant.parse("2026-10-07T13:00:00Z"), 2, 1))
        }
        override suspend fun resendLogin(challengeId: String): AccessResult<Unit> {
            resends++; this.challengeId = challengeId; return AccessResult.Success(Unit)
        }
        override suspend fun recoverPassword(email: String): AccessResult<Unit> {
            recoveries++; this.email = email; return recoveryResult
        }
        override suspend fun logout(token: String): AccessResult<Unit> { revokedToken = token; return logoutResult }
    }
}
