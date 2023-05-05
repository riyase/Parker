package com.keepqueue.sparepark.data.model

data class Booking(
    val id: Int,
    val spaceId: Int = 0,
    val spaceName: String = "",
    val timeFrom: String,
    val timeTo: String,
    val status: String,
    val type: String?,
    val userName: String?,
    val rating: Int,
    val review: String
)
