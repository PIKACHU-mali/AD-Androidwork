package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.NetworkConfig
import com.example.nus.api.ViewRequestResponse
import com.example.nus.model.RequestStatus
import com.example.nus.model.ViewRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ViewRequestViewModel : ViewModel() {
    private val _viewRequests = mutableStateListOf<ViewRequest>()
    val viewRequests: List<ViewRequest> get() = _viewRequests

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)
    val successMessage = mutableStateOf<String?>(null)

    // API service for future backend integration
    private val apiService = NetworkConfig.apiService

    init {
        loadViewRequests()
    }

    fun loadViewRequests() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // TODO: Replace with real API call when backend is ready
                // val response = apiService.getViewRequests()
                // if (response.isSuccessful) {
                //     _viewRequests.clear()
                //     _viewRequests.addAll(response.body() ?: emptyList())
                // } else {
                //     errorMessage.value = "Failed to load requests: ${response.message()}"
                // }

                // For now, use mock data for demonstration
                delay(1000) // Simulate network delay
                _viewRequests.clear()
                _viewRequests.addAll(getMockViewRequests())

            } catch (e: Exception) {
                errorMessage.value = "Failed to load requests: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun acceptRequest(request: ViewRequest, responseMessage: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // TODO: Replace with real API call when backend is ready
                // val response = apiService.acceptViewRequest(
                //     request.id,
                //     ViewRequestResponse(responseMessage)
                // )
                // if (response.isSuccessful) {
                //     val updatedRequest = response.body()
                //     if (updatedRequest != null) {
                //         val index = _viewRequests.indexOfFirst { it.id == request.id }
                //         if (index >= 0) {
                //             _viewRequests[index] = updatedRequest
                //         }
                //     }
                //     successMessage.value = "Request accepted successfully"
                // } else {
                //     errorMessage.value = "Failed to accept request: ${response.message()}"
                // }

                // For now, use mock implementation
                delay(500) // Simulate network delay
                val updatedRequest = request.copy(
                    status = RequestStatus.ACCEPTED,
                    responseMessage = responseMessage,
                    responseDate = LocalDateTime.now()
                )

                val index = _viewRequests.indexOfFirst { it.id == request.id }
                if (index >= 0) {
                    _viewRequests[index] = updatedRequest
                }

                successMessage.value = "Request accepted successfully"

            } catch (e: Exception) {
                errorMessage.value = "Failed to accept request: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun rejectRequest(request: ViewRequest, responseMessage: String) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // TODO: Replace with real API call when backend is ready
                // val response = apiService.rejectViewRequest(
                //     request.id,
                //     ViewRequestResponse(responseMessage)
                // )
                // if (response.isSuccessful) {
                //     val updatedRequest = response.body()
                //     if (updatedRequest != null) {
                //         val index = _viewRequests.indexOfFirst { it.id == request.id }
                //         if (index >= 0) {
                //             _viewRequests[index] = updatedRequest
                //         }
                //     }
                //     successMessage.value = "Request rejected successfully"
                // } else {
                //     errorMessage.value = "Failed to reject request: ${response.message()}"
                // }

                // For now, use mock implementation
                delay(500) // Simulate network delay
                val updatedRequest = request.copy(
                    status = RequestStatus.REJECTED,
                    responseMessage = responseMessage,
                    responseDate = LocalDateTime.now()
                )

                val index = _viewRequests.indexOfFirst { it.id == request.id }
                if (index >= 0) {
                    _viewRequests[index] = updatedRequest
                }

                successMessage.value = "Request rejected successfully"

            } catch (e: Exception) {
                errorMessage.value = "Failed to reject request: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }

    // Mock data for demonstration purposes
    // In real implementation, this would be replaced by API calls
    private fun getMockViewRequests(): List<ViewRequest> {
        return listOf(
            ViewRequest(
                id = "1",
                counsellorId = "counsellor_001",
                counsellorName = "Dr. Sarah Johnson",
                counsellorEmail = "sarah.johnson@mentalhealth.com",
                counsellorLicense = "LPC-12345",
                requestMessage = "Hello, I am Dr. Sarah Johnson, a licensed professional counselor. I would like to request access to view your mood and lifestyle data to better understand your mental health patterns and provide you with personalized support. This access will help me track your progress and adjust our therapy sessions accordingly.",
                requestDate = LocalDateTime.now().minusDays(2),
                status = RequestStatus.PENDING
            ),
            ViewRequest(
                id = "2",
                counsellorId = "counsellor_002",
                counsellorName = "Dr. Michael Chen",
                counsellorEmail = "michael.chen@therapy.org",
                counsellorLicense = "LMFT-67890",
                requestMessage = "Hi there! I'm Dr. Michael Chen, a licensed marriage and family therapist. I'm reaching out to request permission to access your mental health tracking data. This will allow me to provide more targeted and effective therapeutic interventions based on your daily mood patterns and lifestyle factors.",
                requestDate = LocalDateTime.now().minusDays(1),
                status = RequestStatus.PENDING
            ),
            ViewRequest(
                id = "3",
                counsellorId = "counsellor_003",
                counsellorName = "Dr. Emily Rodriguez",
                counsellorEmail = "emily.rodriguez@wellness.com",
                counsellorLicense = "LCSW-11111",
                requestMessage = "Good day! I am Dr. Emily Rodriguez, a licensed clinical social worker specializing in anxiety and depression treatment. I would appreciate access to your mood tracking data to help monitor your progress and identify any concerning patterns that we should address in our sessions.",
                requestDate = LocalDateTime.now().minusHours(6),
                status = RequestStatus.ACCEPTED,
                responseMessage = "Thank you for your request. I'm comfortable sharing my data with you to improve my treatment.",
                responseDate = LocalDateTime.now().minusHours(2)
            )
        )
    }
}