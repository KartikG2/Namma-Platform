package com.namma.platform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ERailTrainsResponse(
    @SerialName("status") val status: Boolean = false,
    @SerialName("message") val message: String = "",
    @SerialName("data") val data: List<ERailTrainDto> = emptyList()
)

@Serializable
data class ERailTrainDto(
    @SerialName("trainNumber") val trainNo: String = "",
    @SerialName("trainName") val trainName: String = "",
    @SerialName("departureTime") val depTime: String = "",
    @SerialName("arrivalTime") val arrTime: String = "",
    @SerialName("dstn_stn_code") val toStn: String = "",
    @SerialName("src_stn_code") val fromStn: String = ""
)
