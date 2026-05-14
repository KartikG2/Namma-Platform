package com.namma.platform.data.remote.mapper

import com.namma.platform.data.remote.dto.ERailLiveStatusDto
import com.namma.platform.data.remote.dto.ERailScheduleDto
import com.namma.platform.data.remote.dto.ERailTrainDto
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.StationEta
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.model.TrainPosition

/**
 * Mapper for eRail API DTOs to Domain models.
 */
object ERailMapper {

    fun ERailTrainDto.toDomain(): Train {
        return Train(
            trainNo = trainNo,
            trainName = trainName,
            trainNameKannada = trainName,
            departureTime = depTime,
            platformNo = 0, // RapidAPI station list might not provide platform
            delayMinutes = 0,
            coaches = emptyList()
        )
    }

    fun ERailLiveStatusDto.toDomain(
        lastStn: Station?,
        nextStn: Station?,
        routeStations: List<StationEta>
    ): TrainPosition {
        val dummyStn = Station("", "", "", 0.0, 0.0)
        return TrainPosition(
            trainNo = trainNo,
            lastStation = lastStn ?: dummyStn,
            nextStation = nextStn ?: dummyStn,
            delayMinutes = delay,
            updatedAt = System.currentTimeMillis(),
            upcomingStations = routeStations
        )
    }

    fun ERailScheduleDto.toDomain(station: Station): StationEta {
        return StationEta(
            station = station,
            etaMinutes = 0 // In a real app we might calculate eta from arrivalTime or live status
        )
    }
}
