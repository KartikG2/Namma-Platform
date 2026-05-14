package com.namma.platform.data.remote.dto

import com.namma.platform.domain.model.CoachLayout
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.StationEta
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.model.TrainPosition

/**
 * Extension functions to map DTOs → domain models.
 *
 * These adapt the API response shapes to the new domain
 * models that use Kannada / English names and simplified fields.
 */

fun TrainDto.toDomain(): Train = Train(
    trainNo = trainNo,
    trainName = trainName,
    trainNameKannada = trainName, // API may not provide Kannada; fallback to English
    departureTime = departureTime,
    platformNo = platform?.toIntOrNull() ?: 0,
    delayMinutes = delay,
    coaches = emptyList()
)

fun StationDto.toDomain(): Station = Station(
    code = code,
    nameKannada = name,  // API may not provide Kannada; fallback to English
    nameEnglish = name,
    lat = latitude,
    lng = longitude,
    zone = zone
)

fun TrainPositionResponse.toDomain(): TrainPosition {
    val lastStn = currentStation?.toDomain() ?: Station("", "", "", 0.0, 0.0)
    val nextStn = nextStation?.toDomain() ?: Station("", "", "", 0.0, 0.0)

    return TrainPosition(
        trainNo = trainNo,
        lastStation = lastStn,
        nextStation = nextStn,
        delayMinutes = delay,
        updatedAt = System.currentTimeMillis(),
        upcomingStations = route
            .filter { !it.isPassed }
            .map { routeDto ->
                StationEta(
                    station = routeDto.station.toDomain(),
                    etaMinutes = routeDto.haltMinutes // placeholder; real ETA needs calculation
                )
            }
    )
}

fun CoachLayoutResponse.toDomain(): CoachLayout = CoachLayout(
    trainNo = trainNo,
    trainName = trainNo,  // Name not in this response; caller can enrich
    coaches = coaches.map { it.coachNumber }
)
