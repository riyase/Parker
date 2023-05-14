package com.keepqueue.sparepark.data.response

import com.keepqueue.sparepark.data.model.Space

data class GetSpacesResponse(
    val status: Boolean,
    val message: String,
    val spaces: List<Space>
)
