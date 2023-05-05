package com.keepqueue.sparepark.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Space(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val type: String,
    val hourRate: Double,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val postCode: String,
    val description: String?,
    val rating: Double,
    val review: String?
) : Parcelable
