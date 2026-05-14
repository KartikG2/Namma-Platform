package com.namma.platform.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class StationStatus {
    PASSED, CURRENT, UPCOMING
}

@Serializable
data class RouteStation(
    val station: Station,
    val status: StationStatus,
    val scheduledArrival: String,
    val actualArrival: String,
    val delayMins: Int,
    var announced5km: Boolean = false,
    var announced2km: Boolean = false,
    var announcedArrival: Boolean = false
)
