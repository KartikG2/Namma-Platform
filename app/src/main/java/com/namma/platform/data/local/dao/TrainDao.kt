package com.namma.platform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namma.platform.data.local.entity.TrainEntity

/**
 * Data Access Object for cached train data.
 */
@Dao
interface TrainDao {

    // ── Queries ──

    @Query("""
        SELECT * FROM trains
        WHERE stationCode = :stationCode
        ORDER BY departureTime ASC
    """)
    suspend fun getTrainsForStation(stationCode: String): List<TrainEntity>

    @Query("SELECT * FROM trains WHERE trainNo = :trainNo LIMIT 1")
    suspend fun getTrainByNo(trainNo: String): TrainEntity?

    // ── Writes ──

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrains(trains: List<TrainEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrain(train: TrainEntity)

    // ── Cleanup ──

    /**
     * Delete trains cached before the given epoch millis.
     * Call periodically to keep the cache fresh.
     */
    @Query("DELETE FROM trains WHERE cachedAt < :olderThan")
    suspend fun deleteOldTrains(olderThan: Long)

    @Query("DELETE FROM trains WHERE stationCode = :stationCode")
    suspend fun deleteTrainsForStation(stationCode: String)

    @Query("DELETE FROM trains")
    suspend fun deleteAll()
}
