package com.namma.platform.domain.model

/**
 * Real-time position of a running train.
 *
 * @property trainNo          Train number
 * @property lastStation      Last reported station
 * @property nextStation      Next upcoming station
 * @property delayMinutes     Current delay
 * @property updatedAt        Epoch millis of last position update
 * @property upcomingStations Ordered list of stations ahead with ETAs
 */
data class TrainPosition(
    val trainNo: String,
    val trainName: String = "",
    val lastStation: Station,
    val nextStation: Station,
    val delayMinutes: Int,
    val updatedAt: Long,
    val fullRoute: List<RouteStation> = emptyList(),
    val upcomingStations: List<StationEta> = emptyList()
)

/**
 * A station along the route with estimated time of arrival.
 *
 * @property station    The station
 * @property etaMinutes Estimated minutes until arrival
 */
data class StationEta(
    val station: Station,
    val etaMinutes: Int
)
