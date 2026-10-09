package pe.edu.upc.saludya.features.iam

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pe.edu.upc.saludya.features.iam.domain.model.*
import pe.edu.upc.saludya.features.iam.domain.repository.*
import pe.edu.upc.saludya.features.iam.presentation.registration.*
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val identity = VerifiedIdentity(IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1)))
    private val repository = FakeRepository()
    private lateinit var viewModel: RegistrationViewModel
    @Before fun setup() { Dispatchers.setMain(dispatcher); viewModel = RegistrationViewModel(repository) }
    @After fun teardown() { Dispatchers.resetMain() }
    private fun validForm() {
        viewModel.prepare(identity)
        viewModel.edit(CredentialsField.EMAIL, " LUCIA@example.com ")
        viewModel.edit(CredentialsField.PASSWORD, "SaludYa123")
        viewModel.edit(CredentialsField.CONFIRM_PASSWORD, "SaludYa123")
        viewModel.edit(CredentialsField.PHONE, "987654321")
    }

    @Test fun `sending a code does not create a patient account`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        assertTrue(viewModel.state.value.awaitingCode)
        assertNull(viewModel.state.value.receipt)
        assertEquals(0, repository.registrations)
        assertEquals("lucia@example.com", repository.recipient)
        assertFalse(viewModel.state.value.canContinue)
    }
    @Test fun `valid code creates account once and clears secrets`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        viewModel.editCode("123456"); viewModel.verify(); viewModel.verify(); advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
        assertEquals(1, repository.registrations)
        assertEquals("", viewModel.state.value.form.password)
        assertEquals("", viewModel.state.value.code)
    }
    @Test fun `wrong code retains pending credentials and allows retry`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        repository.result = RegistrationResult.InvalidCode
        viewModel.editCode("000000"); viewModel.verify(); advanceUntilIdle()
        assertNull(viewModel.state.value.receipt)
        assertEquals(RegistrationStatus.INVALID_CODE, viewModel.state.value.status)
        assertTrue(viewModel.state.value.awaitingCode)
        repository.result = null
        viewModel.editCode("123456"); viewModel.verify(); advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
    }
    @Test fun `invalid code format never reaches backend`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        for (code in listOf("123", "abcdef", "１２３４５６", "1234567")) {
            viewModel.editCode(code); viewModel.verify(); advanceUntilIdle()
            assertEquals(RegistrationStatus.CODE_FORMAT, viewModel.state.value.status)
        }
        assertEquals(0, repository.registrations)
    }
    @Test fun `resend clears old input and uses the same recipient`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        viewModel.editCode("123456"); viewModel.resend(); viewModel.resend(); advanceUntilIdle()
        assertEquals(2, repository.dispatches)
        assertEquals("", viewModel.state.value.code)
        assertEquals("lucia@example.com", repository.recipient)
        assertEquals(0, repository.registrations)
    }
    @Test fun `email already registered returns to the correct editable field`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        repository.result = RegistrationResult.EmailInUse
        viewModel.editCode("123456"); viewModel.verify(); advanceUntilIdle()
        assertFalse(viewModel.state.value.awaitingCode)
        assertEquals(CredentialsError.EMAIL_IN_USE, viewModel.state.value.errors[CredentialsField.EMAIL])
        viewModel.edit(CredentialsField.EMAIL, "otro@example.com")
        assertTrue(viewModel.state.value.errors.isEmpty())
    }
    @Test fun `invalid credentials never request a code`() = runTest(dispatcher) {
        validForm(); viewModel.edit(CredentialsField.CONFIRM_PASSWORD, "Different123")
        viewModel.edit(CredentialsField.EMAIL, "invalid"); viewModel.edit(CredentialsField.PHONE, "123")
        viewModel.submit(); advanceUntilIdle()
        assertEquals(0, repository.dispatches)
        assertEquals(3, viewModel.state.value.errors.size)
    }
    @Test fun `missing identity blocks all registration requests`() = runTest(dispatcher) {
        viewModel.submit(); viewModel.verify(); viewModel.resend(); advanceUntilIdle()
        assertEquals(RegistrationStatus.IDENTITY_REQUIRED, viewModel.state.value.status)
        assertEquals(0, repository.dispatches + repository.registrations)
    }
    @Test fun `pending dispatch blocks duplicates editing and back`() = runTest(dispatcher) {
        validForm(); repository.pending = CompletableDeferred()
        viewModel.submit(); viewModel.submit(); viewModel.edit(CredentialsField.EMAIL, "other@example.com")
        viewModel.backToCredentials(); runCurrent()
        assertEquals(1, repository.dispatches)
        assertEquals("lucia@example.com", viewModel.state.value.form.email)
        repository.pending!!.complete(Unit); advanceUntilIdle()
    }
    @Test fun `delivery failure does not show sent or create account`() = runTest(dispatcher) {
        validForm(); repository.dispatchResult = RegistrationResult.Unavailable
        viewModel.submit(); advanceUntilIdle()
        assertFalse(viewModel.state.value.awaitingCode)
        assertEquals(RegistrationStatus.UNAVAILABLE, viewModel.state.value.status)
        assertEquals(0, repository.registrations)
    }
    @Test fun `leaving or changing identity clears credentials and code`() = runTest(dispatcher) {
        validForm(); viewModel.submit(); advanceUntilIdle()
        viewModel.prepare(VerifiedIdentity(identity.details.copy(dni = "87654321")))
        assertEquals(CredentialsForm(), viewModel.state.value.form)
        viewModel.reset(); assertNull(viewModel.state.value.identity)
    }
    private class FakeRepository : RegistrationRepository {
        var dispatches = 0; var registrations = 0; var recipient = ""
        var result: RegistrationResult? = null
        var dispatchResult: RegistrationResult = RegistrationResult.CodeSent
        var pending: CompletableDeferred<Unit>? = null
        override suspend fun sendCode(email: String): RegistrationResult {
            dispatches++; recipient = email; pending?.await(); return dispatchResult
        }
        override suspend fun register(identity: VerifiedIdentity, credentials: RegistrationCredentials, code: String): RegistrationResult {
            registrations++
            return result ?: RegistrationResult.Accepted(RegistrationReceipt(1, 2, credentials.email))
        }
    }
}
