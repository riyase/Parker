package com.keepqueue.sparepark.data.response

import com.keepqueue.sparepark.data.model.Space

data class SearchSpaceResponse(
    val status: Boolean,
    val spaces: List<Space>
)
