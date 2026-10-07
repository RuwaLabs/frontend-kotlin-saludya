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
import pe.edu.upc.saludya.iam.presentation.identity.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class IdentityViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val clock = Clock.fixed(Instant.parse("2026-10-07T12:00:00Z"), ZoneId.of("America/Lima"))
    private val repository = FakeRepository()
    private lateinit var viewModel: IdentityViewModel

    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = IdentityViewModel(repository, clock)
    }
    @After fun teardown() { Dispatchers.resetMain() }

    private fun validForm() {
        viewModel.edit(IdentityField.DNI, "01234567")
        viewModel.edit(IdentityField.NAMES, "Lucía")
        viewModel.edit(IdentityField.LAST_NAMES, "Torres")
        viewModel.edit(IdentityField.BIRTH_DATE, "01/05/2000")
    }

    @Test fun `scenario 1 verified identity enables continuation`() = runTest(dispatcher) {
        validForm()
        viewModel.verify()
        assertEquals(IdentityStatus.VERIFYING, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canContinue)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
        assertEquals("01234567", viewModel.state.value.verifiedIdentity?.details?.dni)
        assertEquals(1, repository.calls)
    }

    @Test fun `scenario 2 mismatch blocks continuation and allows correction`() = runTest(dispatcher) {
        validForm()
        repository.result = IdentityVerificationResult.Mismatch
        viewModel.verify()
        advanceUntilIdle()
        assertEquals(IdentityStatus.MISMATCH, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canContinue)
        viewModel.edit(IdentityField.NAMES, "Nombre corregido")
        repository.result = null
        viewModel.verify()
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
    }

    @Test fun `scenario 3 invalid DNI never consults the backend`() = runTest(dispatcher) {
        validForm()
        viewModel.edit(IdentityField.DNI, "12345abc")
        viewModel.verify()
        advanceUntilIdle()
        assertEquals(0, repository.calls)
        assertEquals(IdentityValidationError.DNI_FORMAT, viewModel.state.value.errors[IdentityField.DNI])
        assertFalse(viewModel.state.value.canContinue)
    }

    @Test fun `scenario 4 outage blocks continuation and permits retry`() = runTest(dispatcher) {
        validForm()
        repository.result = IdentityVerificationResult.Unavailable
        viewModel.verify()
        advanceUntilIdle()
        assertEquals(IdentityStatus.UNAVAILABLE, viewModel.state.value.status)
        assertFalse(viewModel.state.value.canContinue)
        repository.result = null
        viewModel.verify()
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
        assertEquals(2, repository.calls)
    }

    @Test fun `editing any verified field revokes verification`() = runTest(dispatcher) {
        for (field in IdentityField.entries) {
            validForm()
            viewModel.verify()
            advanceUntilIdle()
            assertTrue(viewModel.state.value.canContinue)
            viewModel.edit(field, "changed")
            assertFalse(viewModel.state.value.canContinue)
            assertNull(viewModel.state.value.verifiedIdentity)
        }
    }

    @Test fun `duplicate taps and edits cannot race a pending verification`() = runTest(dispatcher) {
        validForm()
        repository.pending = CompletableDeferred()
        viewModel.verify()
        viewModel.verify()
        viewModel.edit(IdentityField.DNI, "87654321")
        runCurrent()
        assertEquals(1, repository.calls)
        assertEquals("01234567", viewModel.state.value.form.dni)
        repository.pending?.complete(Unit)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.canContinue)
    }

    private class FakeRepository : IdentityRepository {
        var calls = 0
        var result: IdentityVerificationResult? = null
        var pending: CompletableDeferred<Unit>? = null
        override suspend fun verify(details: IdentityDetails): IdentityVerificationResult {
            calls++
            pending?.await()
            return result ?: IdentityVerificationResult.Verified(VerifiedIdentity(details))
        }
    }
}
