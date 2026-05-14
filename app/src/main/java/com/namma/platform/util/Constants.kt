package com.namma.platform.util

/**
 * App-wide constants.
 */
object Constants {

    // API Base URL - replace with actual eRail API base URL
    const val BASE_URL = "https://api.erail.in/v1/"

    // DataStore
    const val DATASTORE_NAME = "namma_preferences"

    // Preferences Keys
    const val PREF_LAST_STATION = "last_station_code"
    const val PREF_DARK_MODE = "dark_mode"
    const val PREF_TTS_ENABLED = "tts_enabled"
    const val PREF_LANGUAGE = "language"

    // Date Formats
    const val DATE_FORMAT_API = "yyyyMMdd"
    const val DATE_FORMAT_DISPLAY = "dd MMM yyyy"
    const val TIME_FORMAT_DISPLAY = "HH:mm"

    // Map Defaults
    const val DEFAULT_MAP_ZOOM = 10.0
    const val INDIA_CENTER_LAT = 20.5937
    const val INDIA_CENTER_LNG = 78.9629

    // Refresh Intervals (ms)
    const val TRAIN_POSITION_REFRESH_INTERVAL = 30_000L   // 30 seconds
    const val DASHBOARD_REFRESH_INTERVAL = 60_000L         // 1 minute
}
