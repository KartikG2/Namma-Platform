package com.namma.platform.domain.model

/**
 * Coach composition of a train.
 *
 * @property trainNo   Train number
 * @property trainName Train name in English
 * @property coaches   Ordered list of coach codes from engine to guard
 *                     (e.g. ["LOCO","EOG","A1","A2","B1","B2","S1",…,"SLR"])
 */
data class CoachLayout(
    val trainNo: String,
    val trainName: String,
    val coaches: List<String>
)
