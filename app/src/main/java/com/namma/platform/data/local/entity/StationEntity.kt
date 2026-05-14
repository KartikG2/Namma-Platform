package com.namma.platform.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.namma.platform.domain.model.Station

/**
 * Room entity for cached station data.
 * Maps 1:1 to the [Station] domain model plus local-only metadata.
 */
@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey
    val code: String,
    val nameKannada: String,
    val nameEnglish: String,
    val lat: Double,
    val lng: Double,
    val zone: String = "",
    // ── local-only metadata ──
    val isFavorite: Boolean = false,
    val lastSearched: Long = 0L
) {
    /** Convert to domain model. */
    fun toDomain(): Station = Station(
        code = code,
        nameKannada = nameKannada,
        nameEnglish = nameEnglish,
        lat = lat,
        lng = lng,
        zone = zone
    )

    companion object {
        /** Create entity from domain model. */
        fun fromDomain(station: Station): StationEntity = StationEntity(
            code = station.code,
            nameKannada = station.nameKannada,
            nameEnglish = station.nameEnglish,
            lat = station.lat,
            lng = station.lng,
            zone = station.zone
        )
    }
}
