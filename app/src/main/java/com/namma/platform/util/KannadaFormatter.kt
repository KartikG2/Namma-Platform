package com.namma.platform.util

object KannadaFormatter {

    private val numberMap = mapOf(
        0 to "sonne",
        1 to "ondu",
        2 to "eradu",
        3 to "mooru",
        4 to "naalku",
        5 to "aidu",
        6 to "aaru",
        7 to "elu",
        8 to "entu",
        9 to "ombattu",
        10 to "hattu",
        11 to "hadinnondu",
        12 to "hanneradu",
        13 to "hadimooru",
        14 to "hadinalku",
        15 to "hadaidu",
        16 to "hadardu",
        17 to "hadyelu",
        18 to "hadinenttu",
        19 to "hattombattu",
        20 to "ippattu",
        30 to "muvattu",
        40 to "nalavattu",
        50 to "aimvattu",
        60 to "aravattu",
        70 to "eppattu",
        80 to "embattu",
        90 to "tombattu",
        100 to "nooru"
    )

    fun formatNumber(number: Int): String {
        if (number <= 20) return numberMap[number] ?: number.toString()
        if (number < 100) {
            val tens = (number / 10) * 10
            val ones = number % 10
            return if (ones == 0) {
                numberMap[tens] ?: number.toString()
            } else {
                "${numberMap[tens]} ${numberMap[ones]}"
            }
        }
        if (number == 100) return numberMap[100]!!
        return number.toString()
    }

    fun formatTrainNo(trainNo: String): String {
        return trainNo.map { char ->
            val digit = char.toString().toIntOrNull()
            if (digit != null) numberMap[digit] else char.toString()
        }.joinToString(" ")
    }

    fun formatTime(timeStr: String): String {
        // timeStr: "HH:mm"
        val parts = timeStr.split(":")
        if (parts.size != 2) return timeStr
        val hours = parts[0].toIntOrNull() ?: 0
        val minutes = parts[1].toIntOrNull() ?: 0
        
        val hourText = formatNumber(hours)
        val minuteText = formatNumber(minutes)
        
        return "$hourText gante $minuteText nimisha"
    }
}
