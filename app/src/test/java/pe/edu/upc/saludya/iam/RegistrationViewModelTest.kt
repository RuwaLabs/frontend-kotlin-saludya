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
import pe.edu.upc.saludya.iam.presentation.registration.*
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val identity = VerifiedIdentity(IdentityDetails("01234567", "Lucía", "Torres", LocalDate.of(2000, 5, 1)))
    private val repository = FakeRepository()
    private lateinit var viewModel: RegistrationViewModel

    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = RegistrationViewModel(repository)
    }
    @After fun teardown() { Dispatchers.resetMain() }

    private fun validForm() {
        viewModel.prepare(identity)
        viewModel.edit(CredentialsField.EMAIL, "lucia@example.com")
        viewModel.edit(CredentialsField.PASSWORD, "SaludYa123")
        viewModel.edit(CredentialsField.CONFIRM_PASSWORD, "SaludYa123")
        viewModel.edit(CredentialsField.PHONE, "987654321")
    }

    @Test fun `verified identity and valid credentials advance to the email step`() = runTest(dispatcher) {
        validForm()
        viewModel.submit()
        assertEquals(RegistrationStatus.SUBMITTING, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canContinue)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
        assertEquals("lucia@example.com", viewModel.state.value.receipt?.email)
        assertEquals("", viewModel.state.value.form.password)
        assertEquals("", viewModel.state.value.form.confirmPassword)
        assertEquals(identity, repository.lastIdentity)
    }

    @Test fun `email already registered marks the email field and allows correction`() = runTest(dispatcher) {
        validForm()
        repository.result = RegistrationResult.EmailInUse
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(CredentialsError.EMAIL_IN_USE, viewModel.state.value.errors[CredentialsField.EMAIL])
        assertFalse(viewModel.state.value.canContinue)
        viewModel.edit(CredentialsField.EMAIL, "otro@example.com")
        assertFalse(viewModel.state.value.errors.containsKey(CredentialsField.EMAIL))
        repository.result = null
        viewModel.submit()
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
    }

    @Test fun `mismatched passwords never submit a request`() = runTest(dispatcher) {
        validForm()
        viewModel.edit(CredentialsField.CONFIRM_PASSWORD, "Different123")
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(0, repository.calls)
        assertEquals(CredentialsError.PASSWORD_MISMATCH, viewModel.state.value.errors[CredentialsField.CONFIRM_PASSWORD])
    }

    @Test fun `invalid email and phone identify both fields without calling backend`() = runTest(dispatcher) {
        validForm()
        viewModel.edit(CredentialsField.EMAIL, "invalid")
        viewModel.edit(CredentialsField.PHONE, "123")
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(0, repository.calls)
        assertEquals(CredentialsError.EMAIL_FORMAT, viewModel.state.value.errors[CredentialsField.EMAIL])
        assertEquals(CredentialsError.PHONE_FORMAT, viewModel.state.value.errors[CredentialsField.PHONE])
    }

    @Test fun `registration is inaccessible without verified identity`() = runTest(dispatcher) {
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(RegistrationStatus.IDENTITY_REQUIRED, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canSubmit)
        assertEquals(0, repository.calls)
    }

    @Test fun `duplicate taps and editing cannot race a pending submission`() = runTest(dispatcher) {
        validForm()
        repository.pending = CompletableDeferred()
        viewModel.submit()
        viewModel.submit()
        viewModel.edit(CredentialsField.EMAIL, "otro@example.com")
        runCurrent()
        assertEquals(1, repository.calls)
        assertEquals("lucia@example.com", viewModel.state.value.form.email)
        repository.pending?.complete(Unit)
        advanceUntilIdle()
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(1, repository.calls)
    }

    @Test fun `outage keeps continuation blocked and preserves data for retry`() = runTest(dispatcher) {
        validForm()
        repository.result = RegistrationResult.Unavailable
        viewModel.submit()
        advanceUntilIdle()
        assertEquals(RegistrationStatus.UNAVAILABLE, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canContinue)
        assertEquals("987654321", viewModel.state.value.form.phone)
    }

    @Test fun `changing verified identity clears the previous credentials and receipt`() = runTest(dispatcher) {
        validForm()
        viewModel.submit()
        advanceUntilIdle()
        viewModel.prepare(VerifiedIdentity(identity.details.copy(dni = "87654321")))
        assertEquals(CredentialsForm(), viewModel.state.value.form)
        assertNull(viewModel.state.value.receipt)
        assertFalse(viewModel.state.value.canContinue)
    }

    private class FakeRepository : RegistrationRepository {
        var calls = 0
        var result: RegistrationResult? = null
        var pending: CompletableDeferred<Unit>? = null
        var lastIdentity: VerifiedIdentity? = null
        override suspend fun register(identity: VerifiedIdentity, credentials: RegistrationCredentials): RegistrationResult {
            calls++
            lastIdentity = identity
            pending?.await()
            return result ?: RegistrationResult.Accepted(RegistrationReceipt(1, 2, credentials.email))
        }
    }
}
