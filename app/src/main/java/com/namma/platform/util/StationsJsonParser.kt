package com.namma.platform.util

import android.content.Context
import com.namma.platform.domain.model.Station
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Parses `assets/stations.json` into a list of [Station] domain models.
 *
 * Usage:
 * ```
 * val stations = StationsJsonParser.parse(context)
 * ```
 */
object StationsJsonParser {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * Read and parse `assets/stations.json`.
     *
     * @param context Application or Activity context
     * @return List of [Station] domain models, or empty list on error
     */
    fun parse(context: Context): List<Station> {
        return try {
            val jsonString = context.assets
                .open("stations.json")
                .bufferedReader()
                .use { it.readText() }

            val dtos = json.decodeFromString<List<StationJsonDto>>(jsonString)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // ── Internal DTO matching the JSON shape ──

    @Serializable
    private data class StationJsonDto(
        @SerialName("code") val code: String,
        @SerialName("nameKannada") val nameKannada: String,
        @SerialName("nameEnglish") val nameEnglish: String,
        @SerialName("lat") val lat: Double,
        @SerialName("lng") val lng: Double,
        @SerialName("zone") val zone: String = ""
    ) {
        fun toDomain(): Station = Station(
            code = code,
            nameKannada = nameKannada,
            nameEnglish = nameEnglish,
            lat = lat,
            lng = lng,
            zone = zone
        )
    }
}
