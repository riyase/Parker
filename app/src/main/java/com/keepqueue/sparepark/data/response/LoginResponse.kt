package com.keepqueue.sparepark.data.response

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val userId: Int,
    val userName: String
)
