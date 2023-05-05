package com.keepqueue.sparepark.data.response

import com.keepqueue.sparepark.data.model.Booking

data class GetBookingsResponse(
    val status: Boolean,
    val bookings: List<Booking>
)
