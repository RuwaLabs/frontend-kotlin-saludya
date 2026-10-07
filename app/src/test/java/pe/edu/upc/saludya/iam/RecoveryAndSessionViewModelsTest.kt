package pe.edu.upc.saludya.iam

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.BuildConfig
import pe.edu.upc.saludya.iam.domain.model.*
import pe.edu.upc.saludya.iam.domain.repository.*
import pe.edu.upc.saludya.iam.presentation.access.*
import java.time.*

@OptIn(ExperimentalCoroutinesApi::class)
class RecoveryAndSessionViewModelsTest {
    private val dispatcher = StandardTestDispatcher()
    private val clock = MutableClock()
    private val sessions = PatientSessionStore(clock)
    private val recovery = FakeRecovery()
    private val access = FakeAccess()
    private lateinit var reset: PasswordResetViewModel
    private lateinit var help: RecoveryHelpViewModel
    private val session = PatientSession("signed-token", Instant.parse("2026-10-07T13:00:00Z"), 2, 1)
    private val token = "A".repeat(43)
    private fun link(value: String = token) = "https://${BuildConfig.RESET_LINK_HOST}/reset-password?token=$value"
    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        reset = PasswordResetViewModel(recovery, PasswordResetLink(), sessions)
        help = RecoveryHelpViewModel(recovery)
    }
    @After fun teardown() { Dispatchers.resetMain() }
    private fun passwords() {
        reset.acceptLink(link()); reset.editPassword(" New12345 "); reset.editConfirmation(" New12345 ")
    }
    private fun login(): LoginViewModel { sessions.open(session); return LoginViewModel(access, sessions, clock) }

    @Test fun `valid reset updates password once clears secrets and removes local session`() = runTest(dispatcher) {
        sessions.open(session); passwords(); reset.save(); reset.save(); advanceUntilIdle()
        assertTrue(reset.state.value.updated)
        assertEquals(1, recovery.resets)
        assertEquals(token, recovery.token)
        assertEquals(" New12345 ", recovery.password)
        assertEquals("", reset.state.value.password)
        assertEquals("", reset.state.value.confirmation)
        assertNull(sessions.current())
    }
    @Test fun `different passwords and invalid policy stay local`() = runTest(dispatcher) {
        passwords(); reset.editConfirmation("Other123"); reset.save(); advanceUntilIdle()
        assertTrue(reset.state.value.mismatch); assertEquals(0, recovery.resets)
        reset.editPassword("short"); reset.editConfirmation("short"); reset.save(); advanceUntilIdle()
        assertTrue(reset.state.value.passwordInvalid); assertEquals(0, recovery.resets)
    }
    @Test fun `expired reset link blocks further saves and asks for new link`() = runTest(dispatcher) {
        passwords(); recovery.resetResult = AccessResult.Failure(AccessFailure.RESET_LINK_INVALID)
        reset.save(); advanceUntilIdle()
        assertFalse(reset.state.value.updated)
        assertFalse(reset.state.value.validLink)
        assertEquals(AccessFailure.RESET_LINK_INVALID, reset.state.value.failure)
        assertEquals("", reset.state.value.password)
        reset.save(); advanceUntilIdle(); assertEquals(1, recovery.resets)
    }
    @Test fun `invalid link cannot call reset endpoint`() = runTest(dispatcher) {
        reset.acceptLink("https://foreign.test/reset-password?token=$token")
        reset.editPassword("New12345"); reset.editConfirmation("New12345"); reset.save(); advanceUntilIdle()
        assertFalse(reset.state.value.validLink); assertEquals(0, recovery.resets)
    }
    @Test fun `same incoming link preserves form across recreation and cannot replay success`() = runTest(dispatcher) {
        passwords(); reset.acceptLink(link())
        assertEquals(" New12345 ", reset.state.value.password)
        reset.save(); advanceUntilIdle(); reset.acceptLink(link()); reset.save(); advanceUntilIdle()
        assertTrue(reset.state.value.updated); assertEquals(1, recovery.resets)
    }
    @Test fun `pasted link can be corrected without preserving old token`() = runTest(dispatcher) {
        reset.editLink("not a link"); reset.openPastedLink(); assertFalse(reset.state.value.validLink)
        reset.editLink(link()); reset.openPastedLink(); assertTrue(reset.state.value.validLink)
        reset.reset(); assertFalse(reset.state.value.validLink); assertEquals("", reset.state.value.linkInput)
    }
    @Test fun `reset outage keeps retry possible without claiming an update`() = runTest(dispatcher) {
        passwords(); recovery.resetResult = AccessResult.Failure(AccessFailure.UNAVAILABLE)
        reset.save(); advanceUntilIdle()
        assertFalse(reset.state.value.updated); assertTrue(reset.state.value.validLink)
        recovery.resetResult = AccessResult.Success(Unit); reset.save(); advanceUntilIdle()
        assertTrue(reset.state.value.updated)
    }
    @Test fun `pending reset blocks editing and repeated save`() = runTest(dispatcher) {
        passwords(); recovery.pending = CompletableDeferred()
        reset.save(); reset.save(); reset.editPassword("Changed123"); runCurrent()
        assertEquals(1, recovery.resets); assertEquals(" New12345 ", reset.state.value.password)
        recovery.pending!!.complete(Unit); advanceUntilIdle()
    }
    @Test fun `help identifies missing or invalid DNI and contact email without network`() = runTest(dispatcher) {
        help.submit(); advanceUntilIdle()
        assertTrue(help.state.value.invalidDni); assertTrue(help.state.value.invalidEmail)
        help.editDni("１２３４５６７８"); help.editEmail("bad"); help.submit(); advanceUntilIdle()
        assertEquals(0, recovery.helps)
        assertTrue(help.state.value.invalidDni); assertTrue(help.state.value.invalidEmail)
    }
    @Test fun `valid help records contact request without granting session`() = runTest(dispatcher) {
        help.editDni("01234567"); help.editEmail(" CONTACTO@example.com ")
        help.submit(); help.submit(); advanceUntilIdle(); help.submit(); advanceUntilIdle()
        assertEquals(1, recovery.helps)
        assertEquals("01234567", recovery.dni)
        assertEquals("contacto@example.com", recovery.email)
        assertNotNull(help.state.value.receipt); assertNull(sessions.current())
    }
    @Test fun `help failure remains retryable and never displays registered`() = runTest(dispatcher) {
        help.editDni("01234567"); help.editEmail("contacto@example.com")
        recovery.helpResult = AccessResult.Failure(AccessFailure.UNAVAILABLE)
        help.submit(); advanceUntilIdle(); assertNull(help.state.value.receipt)
        recovery.helpResult = AccessResult.Success(RecoveryHelpReceipt(null, null))
        help.submit(); advanceUntilIdle(); assertNotNull(help.state.value.receipt)
    }
    @Test fun `logout without confirmation or cancellation preserves session and token`() = runTest(dispatcher) {
        val login = login(); login.logout(); advanceUntilIdle()
        assertEquals(0, access.logouts); assertEquals(session, sessions.current())
        login.requestLogout(); assertTrue(login.state.value.logoutConfirmation)
        login.cancelLogout(); login.logout(); advanceUntilIdle()
        assertEquals(0, access.logouts); assertEquals(session, sessions.current())
        assertFalse(login.state.value.logoutConfirmation)
    }
    @Test fun `confirmed logout submits revocation once and clears token`() = runTest(dispatcher) {
        val login = login(); login.requestLogout(); login.logout(); login.logout(); advanceUntilIdle()
        assertEquals(1, access.logouts); assertEquals("signed-token", access.token)
        assertNull(sessions.current()); assertNull(login.state.value.session)
    }
    @Test fun `expired token blocks protected action before HTTP and requires new login`() = runTest(dispatcher) {
        val login = login(); clock.now = session.expiresAt
        login.refreshProfile(); advanceUntilIdle()
        assertEquals(0, access.profiles); assertNull(sessions.current())
        assertEquals(LoginNotice.SESSION_EXPIRED, login.state.value.notice)
        login.reset(); advanceUntilIdle(); assertEquals(LoginNotice.SESSION_EXPIRED, login.state.value.notice)
        login.acknowledgeExpiration(); assertNull(login.state.value.notice)
        assertNull(login.state.value.session)
    }
    @Test fun `server unauthorized expires session even before local token deadline`() = runTest(dispatcher) {
        val login = login(); access.profileResult = AccessResult.Failure(AccessFailure.SESSION_EXPIRED)
        login.refreshProfile(); advanceUntilIdle()
        assertEquals(1, access.profiles); assertNull(sessions.current())
        assertEquals(LoginNotice.SESSION_EXPIRED, login.state.value.notice)
        assertNull(login.state.value.profile)
    }
    @Test fun `profile outage keeps valid session and mismatched account cannot show personal data`() = runTest(dispatcher) {
        val login = login(); access.profileResult = AccessResult.Failure(AccessFailure.UNAVAILABLE)
        login.refreshProfile(); advanceUntilIdle(); assertEquals(session, sessions.current())
        access.profileResult = AccessResult.Success(PatientAccountProfile(999, 999, "Other", "User", "other@example.com"))
        login.refreshProfile(); advanceUntilIdle(); assertNull(login.state.value.profile)
        assertEquals(AccessFailure.UNEXPECTED, login.state.value.failure)
    }
    @Test fun `session invalidation from another feature immediately notifies login route`() = runTest(dispatcher) {
        val login = login(); sessions.expire(); advanceUntilIdle()
        assertEquals(LoginNotice.SESSION_EXPIRED, login.state.value.notice)
        assertNull(login.state.value.session)
    }
    private class MutableClock : Clock() {
        var now = Instant.parse("2026-10-07T12:00:00Z")
        override fun instant(): Instant = now
        override fun getZone(): ZoneId = ZoneOffset.UTC
        override fun withZone(zone: ZoneId): Clock = Clock.fixed(now, zone)
    }
    private class FakeRecovery : RecoveryActionsRepository {
        var resets = 0; var helps = 0; var token = ""; var password = ""; var dni = ""; var email = ""
        var pending: CompletableDeferred<Unit>? = null
        var resetResult: AccessResult<Unit> = AccessResult.Success(Unit)
        var helpResult: AccessResult<RecoveryHelpReceipt> = AccessResult.Success(RecoveryHelpReceipt(null, null))
        override suspend fun resetPassword(token: String, password: String, confirmation: String): AccessResult<Unit> {
            resets++; this.token = token; this.password = password; pending?.await(); return resetResult
        }
        override suspend fun requestHelp(dni: String, contactEmail: String): AccessResult<RecoveryHelpReceipt> {
            helps++; this.dni = dni; email = contactEmail; return helpResult
        }
    }
    private class FakeAccess : AccessRepository {
        var logouts = 0; var profiles = 0; var token = ""
        var profileResult: AccessResult<PatientAccountProfile> = AccessResult.Success(PatientAccountProfile(2, 1, "Lucía", "Torres", "lucia@example.com"))
        override suspend fun profile(token: String): AccessResult<PatientAccountProfile> { profiles++; return profileResult }
        override suspend fun logout(token: String): AccessResult<Unit> { logouts++; this.token = token; return AccessResult.Success(Unit) }
        override suspend fun login(email: String, password: String): AccessResult<LoginChallenge> = AccessResult.Failure(AccessFailure.UNEXPECTED)
        override suspend fun verifyLogin(challengeId: String, code: String): AccessResult<PatientSession> = AccessResult.Failure(AccessFailure.UNEXPECTED)
        override suspend fun resendLogin(challengeId: String): AccessResult<Unit> = AccessResult.Success(Unit)
        override suspend fun recoverPassword(email: String): AccessResult<Unit> = AccessResult.Success(Unit)
    }
}
