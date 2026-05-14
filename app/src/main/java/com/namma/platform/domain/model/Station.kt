package com.namma.platform.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a railway station in Karnataka / India.
 *
 * @property code     IRCTC station code (e.g. "SBC")
 * @property nameKannada  Station name in Kannada (e.g. "ಬೆಂಗಳೂರು ನಗರ")
 * @property nameEnglish  Station name in English (e.g. "Bengaluru City Jn")
 * @property lat      Latitude
 * @property lng      Longitude
 * @property zone     Railway zone (e.g. "SWR")
 */
@Serializable
data class Station(
    val code: String,
    val nameKannada: String,
    val nameEnglish: String,
    val lat: Double,
    val lng: Double,
    val zone: String = ""
) : java.io.Serializable
