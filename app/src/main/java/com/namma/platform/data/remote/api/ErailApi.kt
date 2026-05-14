package com.namma.platform.data.remote.api

import com.namma.platform.data.remote.dto.CoachLayoutResponse
import com.namma.platform.data.remote.dto.StationSearchResponse
import com.namma.platform.data.remote.dto.TrainPositionResponse
import com.namma.platform.data.remote.dto.TrainScheduleResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for the eRail / railway data API.
 */
interface ErailApi {

    @GET("trains-at-station")
    suspend fun getTrainsAtStation(
        @Query("station_code") stationCode: String,
        @Query("hours") hours: Int = 4,
        @Query("api_key") apiKey: String
    ): TrainScheduleResponse

    @GET("live-train-status")
    suspend fun getLiveTrainStatus(
        @Query("train_no") trainNo: String,
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): TrainPositionResponse

    @GET("coach-layout")
    suspend fun getCoachLayout(
        @Query("train_no") trainNo: String,
        @Query("api_key") apiKey: String
    ): CoachLayoutResponse

    @GET("station-search")
    suspend fun searchStations(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): StationSearchResponse
}
