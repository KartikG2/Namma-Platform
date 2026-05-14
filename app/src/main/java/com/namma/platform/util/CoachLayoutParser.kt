package com.namma.platform.util

import android.content.Context
import com.namma.platform.domain.model.CoachLayout
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoachLayoutParser @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun parseAll(): List<CoachLayout> {
        return try {
            val jsonString = context.assets.open("coach_layouts.json").bufferedReader().use { it.readText() }
            val dtos = json.decodeFromString<List<CoachLayoutDto>>(jsonString)
            dtos.map { dto ->
                CoachLayout(
                    trainNo = dto.trainNo,
                    trainName = dto.trainName,
                    coaches = dto.coaches
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getForTrain(trainNo: String): CoachLayout? {
        return parseAll().find { it.trainNo == trainNo }
    }

    @Serializable
    private data class CoachLayoutDto(
        @SerialName("trainNo") val trainNo: String,
        @SerialName("trainName") val trainName: String,
        @SerialName("coaches") val coaches: List<String>
    )
}
