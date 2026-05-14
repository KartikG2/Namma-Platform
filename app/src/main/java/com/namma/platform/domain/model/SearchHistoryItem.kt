package com.namma.platform.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchHistoryItem(
    val trainNo: String = "",
    val trainName: String = "",
    val fromStation: Station,
    val toStation: Station,
    val timestamp: Long = System.currentTimeMillis()
) : java.io.Serializable
