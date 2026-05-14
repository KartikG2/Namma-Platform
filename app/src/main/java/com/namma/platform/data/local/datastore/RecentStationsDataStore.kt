package com.namma.platform.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.namma.platform.domain.model.Station
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "recent_stations_prefs")

@Singleton
class RecentStationsDataStore @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    private val RECENT_STATIONS_KEY = stringPreferencesKey("recent_stations")

    fun getRecentStations(): Flow<List<Station>> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[RECENT_STATIONS_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<Station>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveStation(station: Station) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[RECENT_STATIONS_KEY] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<Station>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }

            // Remove if already exists to move to top
            currentList.removeAll { it.code == station.code }
            
            // Add to top
            currentList.add(0, station)

            // Keep max 3
            if (currentList.size > 3) {
                currentList.removeAt(currentList.size - 1)
            }

            preferences[RECENT_STATIONS_KEY] = Json.encodeToString(currentList)
        }
    }
}
