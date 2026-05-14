package com.namma.platform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.namma.platform.domain.model.Train

/**
 * Room entity for cached train data.
 *
 * Coaches are stored as a comma-separated string because Room
 * doesn't natively support List<String>. Use [toCoachesList] /
 * [fromCoachesList] for conversion.
 */
@Entity(tableName = "trains")
data class TrainEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val trainNo: String,
    val trainName: String,
    val trainNameKannada: String,
    val departureTime: String,
    val platformNo: Int,
    val delayMinutes: Int,
    val coachesCsv: String = "",        // comma-separated coach codes
    val stationCode: String,            // station this train record belongs to
    val cachedAt: Long = System.currentTimeMillis()
) {
    /** Convert to domain model. */
    fun toDomain(): Train = Train(
        trainNo = trainNo,
        trainName = trainName,
        trainNameKannada = trainNameKannada,
        departureTime = departureTime,
        platformNo = platformNo,
        delayMinutes = delayMinutes,
        coaches = toCoachesList()
    )

    /** Parse CSV coaches back to a list. */
    private fun toCoachesList(): List<String> =
        if (coachesCsv.isBlank()) emptyList()
        else coachesCsv.split(",").map { it.trim() }

    companion object {
        /** Create entity from domain model + station context. */
        fun fromDomain(train: Train, stationCode: String): TrainEntity = TrainEntity(
            trainNo = train.trainNo,
            trainName = train.trainName,
            trainNameKannada = train.trainNameKannada,
            departureTime = train.departureTime,
            platformNo = train.platformNo,
            delayMinutes = train.delayMinutes,
            coachesCsv = train.coaches.joinToString(","),
            stationCode = stationCode
        )
    }
}
