package com.namma.platform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ERailLiveStatusResponse(
    @SerialName("status") val status: Boolean = false,
    @SerialName("data") val data: ERailLiveStatusDto? = null
)

@Serializable
data class ERailLiveStatusDto(
    @SerialName("train_number") val trainNo: String = "",
    @SerialName("last_station_name") val lastStationName: String = "",
    @SerialName("next_station_name") val nextStationName: String = "",
    @SerialName("delay") val delay: Int = 0,
    @SerialName("current_location_lat") val lat: Double = 0.0,
    @SerialName("current_location_lng") val lng: Double = 0.0,
    @SerialName("upcoming_stations") val route: List<ERailRouteStationDto> = emptyList()
)

@Serializable
data class ERailRouteStationDto(
    @SerialName("station_code") val stationCode: String = "",
    @SerialName("station_name") val stationName: String = "",
    @SerialName("eta") val eta: String = "0"
)
