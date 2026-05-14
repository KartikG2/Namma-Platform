package com.namma.platform.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.namma.platform.data.local.dao.StationDao
import com.namma.platform.data.local.dao.TrainDao
import com.namma.platform.data.local.entity.StationEntity
import com.namma.platform.data.local.entity.TrainEntity

/**
 * Room database for NammaPlatform.
 *
 * Contains:
 * - [StationEntity] — cached station master data (seeded from assets/stations.json)
 * - [TrainEntity]   — cached train schedule data per station
 */
@Database(
    entities = [
        StationEntity::class,
        TrainEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class NammaDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao
    abstract fun trainDao(): TrainDao

    companion object {
        const val DATABASE_NAME = "namma_platform_db"
    }
}
