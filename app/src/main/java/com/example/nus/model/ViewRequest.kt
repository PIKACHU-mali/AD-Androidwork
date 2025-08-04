package com.example.nus.model

import java.time.LocalDateTime

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

data class ViewRequest(
    val id: String = System.currentTimeMillis().toString(),
    val counsellorId: String,
    val counsellorName: String,
    val counsellorEmail: String,
    val counsellorLicense: String = "", // Professional license number
    val requestMessage: String,
    val requestDate: LocalDateTime = LocalDateTime.now(),
    val status: RequestStatus = RequestStatus.PENDING,
    val responseMessage: String = "",
    val responseDate: LocalDateTime? = null
)