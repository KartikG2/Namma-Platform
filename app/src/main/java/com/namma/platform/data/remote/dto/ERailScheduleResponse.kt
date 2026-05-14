package com.namma.platform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ERailScheduleResponse(
    @SerialName("status") val status: Boolean = false,
    @SerialName("data") val data: ERailScheduleData? = null
)

@Serializable
data class ERailScheduleData(
    @SerialName("station_list") val result: List<ERailScheduleDto> = emptyList()
)

@Serializable
data class ERailScheduleDto(
    @SerialName("station_code") val stationCode: String = "",
    @SerialName("arrival_time") val arrivalTime: String = "",
    @SerialName("departure_time") val departureTime: String = "",
    @SerialName("halt_time") val haltMinutes: String = "0",
    @SerialName("day_count") val day: Int = 0,
    @SerialName("distance") val distance: String = "0"
)
