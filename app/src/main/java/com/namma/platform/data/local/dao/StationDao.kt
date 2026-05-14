package com.namma.platform.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.namma.platform.data.local.entity.StationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for station operations.
 */
@Dao
interface StationDao {

    // ── Queries ──

    @Query("""
        SELECT * FROM stations
        WHERE code LIKE '%' || :query || '%'
           OR nameEnglish LIKE '%' || :query || '%'
           OR nameKannada LIKE '%' || :query || '%'
        ORDER BY nameEnglish ASC
        LIMIT 50
    """)
    suspend fun searchStations(query: String): List<StationEntity>

    @Query("SELECT * FROM stations ORDER BY nameEnglish ASC")
    suspend fun getAllStations(): List<StationEntity>

    @Query("SELECT * FROM stations ORDER BY nameEnglish ASC")
    fun getAllStationsFlow(): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations WHERE code = :code LIMIT 1")
    suspend fun getStationByCode(code: String): StationEntity?

    @Query("SELECT * FROM stations WHERE isFavorite = 1 ORDER BY nameEnglish ASC")
    fun getFavoriteStations(): Flow<List<StationEntity>>

    @Query("SELECT * FROM stations ORDER BY lastSearched DESC LIMIT 10")
    fun getRecentStations(): Flow<List<StationEntity>>

    @Query("SELECT COUNT(*) FROM stations")
    suspend fun getStationCount(): Int

    // ── Writes ──

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: StationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Query("UPDATE stations SET isFavorite = :isFavorite WHERE code = :code")
    suspend fun updateFavorite(code: String, isFavorite: Boolean)

    @Query("UPDATE stations SET lastSearched = :timestamp WHERE code = :code")
    suspend fun updateLastSearched(code: String, timestamp: Long = System.currentTimeMillis())

    // ── Cleanup ──

    @Query("DELETE FROM stations")
    suspend fun deleteAll()
}
