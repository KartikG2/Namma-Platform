package com.namma.platform.data.remote.api

import com.namma.platform.data.remote.dto.ERailLiveStatusResponse
import com.namma.platform.data.remote.dto.ERailScheduleResponse
import com.namma.platform.data.remote.dto.ERailTrainsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Updated to match the IRCTC RapidAPI (irctc1.p.rapidapi.com) structure
 */
interface ERailApiService {

    @GET("api/v3/getLiveStation")
    suspend fun getTrainsAtStation(
        @Query("fromStationCode") stationCode: String,
        @Query("hours") hours: Int = 12
    ): ERailTrainsResponse

    @GET("api/v3/trainBetweenStations")
    suspend fun getTrainsBetweenStations(
        @Query("fromStnCode") fromStation: String,
        @Query("toStnCode") toStation: String,
        @Query("dateOfJourney") date: String
    ): ERailTrainsResponse

    @GET("api/v1/liveTrainStatus")
    suspend fun getLiveTrainStatus(
        @Query("trainNo") trainNo: String,
        @Query("startDay") startDay: Int = 0
    ): ERailLiveStatusResponse

    @GET("api/v1/getTrainSchedule")
    suspend fun getTrainSchedule(
        @Query("trainNo") trainNo: String
    ): ERailScheduleResponse
}
