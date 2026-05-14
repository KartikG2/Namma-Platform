package com.namma.platform.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.namma.platform.domain.model.SearchHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.searchHistoryDataStore by preferencesDataStore(name = "search_history_prefs")

@Singleton
class SearchHistoryDataStore @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context
) {
    private val HISTORY_KEY = stringPreferencesKey("search_history")

    fun getSearchHistory(): Flow<List<SearchHistoryItem>> {
        return context.searchHistoryDataStore.data.map { preferences ->
            val jsonString = preferences[HISTORY_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<SearchHistoryItem>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun saveSearchHistory(item: SearchHistoryItem) {
        context.searchHistoryDataStore.edit { preferences ->
            val currentJson = preferences[HISTORY_KEY] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<SearchHistoryItem>>(currentJson).toMutableList()
            } catch (e: Exception) {
                mutableListOf()
            }

            // Remove if duplicate route (simplified check)
            currentList.removeAll { it.fromStation.code == item.fromStation.code && it.toStation.code == item.toStation.code }
            
            // Add to top
            currentList.add(0, item)

            // Keep max 10
            if (currentList.size > 10) {
                currentList.removeAt(currentList.size - 1)
            }

            preferences[HISTORY_KEY] = Json.encodeToString(currentList)
        }
    }
}
