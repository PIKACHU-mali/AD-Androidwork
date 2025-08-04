package com.example.nus.viewmodel

import com.example.nus.model.RequestStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ViewRequestViewModelTest {

    private lateinit var viewModel: ViewRequestViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ViewRequestViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadViewRequests should populate viewRequests list`() = runTest {
        // Given
        assertTrue("Initial requests list should be empty", viewModel.viewRequests.isEmpty())

        // When
        viewModel.loadViewRequests()
        advanceUntilIdle()

        // Then
        assertFalse("Requests list should not be empty after loading", viewModel.viewRequests.isEmpty())
        assertEquals("Should load 3 mock requests", 3, viewModel.viewRequests.size)
        assertFalse("Loading should be finished", viewModel.isLoading.value)
    }

    @Test
    fun `acceptRequest should update request status to ACCEPTED`() = runTest {
        // Given
        viewModel.loadViewRequests()
        advanceUntilIdle()
        
        val pendingRequest = viewModel.viewRequests.find { it.status == RequestStatus.PENDING }
        assertNotNull("Should have at least one pending request", pendingRequest)
        
        val responseMessage = "Thank you for your request. I accept."

        // When
        viewModel.acceptRequest(pendingRequest!!, responseMessage)
        advanceUntilIdle()

        // Then
        val updatedRequest = viewModel.viewRequests.find { it.id == pendingRequest.id }
        assertNotNull("Request should still exist", updatedRequest)
        assertEquals("Request status should be ACCEPTED", RequestStatus.ACCEPTED, updatedRequest!!.status)
        assertEquals("Response message should be set", responseMessage, updatedRequest.responseMessage)
        assertNotNull("Response date should be set", updatedRequest.responseDate)
        assertEquals("Success message should be set", "Request accepted successfully", viewModel.successMessage.value)
    }

    @Test
    fun `rejectRequest should update request status to REJECTED`() = runTest {
        // Given
        viewModel.loadViewRequests()
        advanceUntilIdle()
        
        val pendingRequest = viewModel.viewRequests.find { it.status == RequestStatus.PENDING }
        assertNotNull("Should have at least one pending request", pendingRequest)
        
        val responseMessage = "I'm not comfortable sharing my data at this time."

        // When
        viewModel.rejectRequest(pendingRequest!!, responseMessage)
        advanceUntilIdle()

        // Then
        val updatedRequest = viewModel.viewRequests.find { it.id == pendingRequest.id }
        assertNotNull("Request should still exist", updatedRequest)
        assertEquals("Request status should be REJECTED", RequestStatus.REJECTED, updatedRequest!!.status)
        assertEquals("Response message should be set", responseMessage, updatedRequest.responseMessage)
        assertNotNull("Response date should be set", updatedRequest.responseDate)
        assertEquals("Success message should be set", "Request rejected successfully", viewModel.successMessage.value)
    }

    @Test
    fun `clearMessages should reset error and success messages`() = runTest {
        // Given
        viewModel.loadViewRequests()
        advanceUntilIdle()
        
        val pendingRequest = viewModel.viewRequests.find { it.status == RequestStatus.PENDING }
        viewModel.acceptRequest(pendingRequest!!, "Test message")
        advanceUntilIdle()
        
        assertNotNull("Success message should be set", viewModel.successMessage.value)

        // When
        viewModel.clearMessages()

        // Then
        assertNull("Success message should be cleared", viewModel.successMessage.value)
        assertNull("Error message should be cleared", viewModel.errorMessage.value)
    }

    @Test
    fun `initial state should be correct`() {
        // Then
        assertTrue("Initial requests list should be empty", viewModel.viewRequests.isEmpty())
        assertFalse("Initial loading state should be false", viewModel.isLoading.value)
        assertNull("Initial error message should be null", viewModel.errorMessage.value)
        assertNull("Initial success message should be null", viewModel.successMessage.value)
    }
}
