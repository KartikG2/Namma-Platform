package com.namma.platform.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ─── Train Schedule Response ───

@Serializable
data class TrainScheduleResponse(
    @SerialName("response_code") val responseCode: Int = 0,
    @SerialName("total") val total: Int = 0,
    @SerialName("trains") val trains: List<TrainDto> = emptyList()
)

@Serializable
data class TrainDto(
    @SerialName("train_no") val trainNo: String = "",
    @SerialName("train_name") val trainName: String = "",
    @SerialName("source") val source: String = "",
    @SerialName("destination") val destination: String = "",
    @SerialName("departure_time") val departureTime: String = "",
    @SerialName("arrival_time") val arrivalTime: String = "",
    @SerialName("platform") val platform: String? = null,
    @SerialName("delay") val delay: Int = 0,
    @SerialName("status") val status: String = "",
    @SerialName("train_type") val trainType: String = "",
    @SerialName("running_days") val runningDays: List<String> = emptyList()
)

// ─── Train Position / Live Status Response ───

@Serializable
data class TrainPositionResponse(
    @SerialName("response_code") val responseCode: Int = 0,
    @SerialName("train_no") val trainNo: String = "",
    @SerialName("train_name") val trainName: String = "",
    @SerialName("current_station") val currentStation: StationDto? = null,
    @SerialName("next_station") val nextStation: StationDto? = null,
    @SerialName("last_updated") val lastUpdated: String = "",
    @SerialName("latitude") val latitude: Double = 0.0,
    @SerialName("longitude") val longitude: Double = 0.0,
    @SerialName("speed") val speed: Double = 0.0,
    @SerialName("delay") val delay: Int = 0,
    @SerialName("status") val status: String = "",
    @SerialName("route") val route: List<RouteStationDto> = emptyList()
)

@Serializable
data class RouteStationDto(
    @SerialName("station") val station: StationDto = StationDto(),
    @SerialName("arrival_time") val arrivalTime: String? = null,
    @SerialName("departure_time") val departureTime: String? = null,
    @SerialName("halt_minutes") val haltMinutes: Int = 0,
    @SerialName("distance") val distance: Int = 0,
    @SerialName("is_passed") val isPassed: Boolean = false,
    @SerialName("platform") val platform: String? = null
)

// ─── Station Search Response ───

@Serializable
data class StationSearchResponse(
    @SerialName("response_code") val responseCode: Int = 0,
    @SerialName("stations") val stations: List<StationDto> = emptyList()
)

@Serializable
data class StationDto(
    @SerialName("code") val code: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("city") val city: String = "",
    @SerialName("state") val state: String = "",
    @SerialName("latitude") val latitude: Double = 0.0,
    @SerialName("longitude") val longitude: Double = 0.0,
    @SerialName("zone") val zone: String = "",
    @SerialName("division") val division: String = ""
)

// ─── Coach Layout Response ───

@Serializable
data class CoachLayoutResponse(
    @SerialName("response_code") val responseCode: Int = 0,
    @SerialName("train_no") val trainNo: String = "",
    @SerialName("coaches") val coaches: List<CoachDto> = emptyList()
)

@Serializable
data class CoachDto(
    @SerialName("coach_number") val coachNumber: String = "",
    @SerialName("coach_type") val coachType: String = "",
    @SerialName("total_seats") val totalSeats: Int = 0,
    @SerialName("available_seats") val availableSeats: Int = 0,
    @SerialName("position") val position: Int = 0,
    @SerialName("amenities") val amenities: List<String> = emptyList()
)
