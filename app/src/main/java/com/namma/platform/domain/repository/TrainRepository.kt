package com.namma.platform.domain.repository

import com.namma.platform.domain.model.CoachLayout
import com.namma.platform.domain.model.Station
import com.namma.platform.domain.model.Train
import com.namma.platform.domain.model.TrainPosition
import kotlinx.coroutines.flow.Flow

interface TrainRepository {
    suspend fun getNextTrains(fromCode: String, toCode: String? = null): Flow<Result<List<Train>>>
    suspend fun getTrainPosition(trainNo: String, date: String): Flow<Result<TrainPosition>>
    suspend fun getCoachLayout(trainNo: String): CoachLayout?
    fun getStations(query: String): Flow<List<Station>>
    fun getRecentStations(): Flow<List<Station>>
    suspend fun saveRecentStation(station: Station)
    fun getSearchHistory(): Flow<List<com.namma.platform.domain.model.SearchHistoryItem>>
    suspend fun saveSearchHistory(item: com.namma.platform.domain.model.SearchHistoryItem)
}
