package com.example.nus.api

import com.example.nus.model.ViewRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * API service interface for communicating with Spring Boot backend
 * This interface defines the endpoints for view request management
 */
interface ApiService {
    
    /**
     * Get all view requests for the current user
     */
    @GET("api/view-requests")
    suspend fun getViewRequests(): Response<List<ViewRequest>>
    
    /**
     * Accept a view request from a counsellor
     */
    @PUT("api/view-requests/{requestId}/accept")
    suspend fun acceptViewRequest(
        @Path("requestId") requestId: String,
        @Body responseMessage: String
    ): Response<ViewRequest>
    
    /**
     * Reject a view request from a counsellor
     */
    @PUT("api/view-requests/{requestId}/reject")
    suspend fun rejectViewRequest(
        @Path("requestId") requestId: String,
        @Body responseMessage: String
    ): Response<ViewRequest>
}

/**
 * Data class for API request body when responding to view requests
 */
data class ViewRequestResponse(
    val responseMessage: String
)
