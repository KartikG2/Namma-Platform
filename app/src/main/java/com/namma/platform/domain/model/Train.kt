package com.namma.platform.domain.model

/**
 * Represents a train arriving at or departing from a station.
 *
 * @property trainNo          Train number (e.g. "12657")
 * @property trainName        Train name in English (e.g. "Mysuru Shatabdi Express")
 * @property trainNameKannada Train name in Kannada (e.g. "ಮೈಸೂರು ಶತಾಬ್ದಿ ಎಕ್ಸ್‌ಪ್ರೆಸ್")
 * @property departureTime    Departure time string "HH:mm"
 * @property platformNo       Platform number (1-based)
 * @property delayMinutes     Current delay in minutes (0 = on time)
 * @property coaches          Ordered list of coach codes (e.g. ["LOCO","EOG","A1","B1"…])
 * @property isOnTime         Computed: true when delayMinutes == 0
 */
data class Train(
    val trainNo: String,
    val trainName: String,
    val trainNameKannada: String,
    val departureTime: String,
    val platformNo: Int,
    val delayMinutes: Int,
    val coaches: List<String> = emptyList(),
    val isOnTime: Boolean = delayMinutes == 0
)
