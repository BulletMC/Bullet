package com.aznos.util

import kotlin.time.Duration

object DurationFormat {
    fun getReadableDuration(duration: Duration?): String {
        if(duration == null) return "permanently"

        val seconds = duration.inWholeSeconds
        val days = seconds / (60 * 60 * 24)
        val hours = (seconds % (60 * 60 * 24)) / (60 * 60)
        val minutes = (seconds % (60 * 60)) / 60
        val secs = seconds % 60

        return buildString {
            append("for ")
            append(appendDays(days))
            append(appendHours(hours))
            append(appendMinutes(minutes))
            append(appendSeconds(secs, days, hours, minutes))
            append(appendZeroSeconds(length))
        }.trimEnd()
    }

    private fun appendDays(days: Long): String {
        return if(days > 0) "$days day${if (days > 1) "s" else ""} " else ""
    }

    private fun appendHours(hours: Long): String {
        return if(hours > 0) "$hours hour${if (hours > 1) "s" else ""} " else ""
    }

    private fun appendMinutes(minutes: Long): String {
        return if(minutes > 0) "$minutes minute${if (minutes > 1) "s" else ""} " else ""
    }

    private fun isOnlySeconds(secs: Long, days: Long, hours: Long, minutes: Long): Boolean {
        return days == 0L && hours == 0L && minutes == 0L && secs > 0
    }

    private fun appendSeconds(secs: Long, days: Long, hours: Long, minutes: Long): String {
        return if(isOnlySeconds(secs, days, hours, minutes)) {
            "$secs second${if (secs > 1) "s" else ""}"
        } else {
            ""
        }
    }

    private fun appendZeroSeconds(length: Int): String {
        return if(length == 3) "0 seconds" else ""
    }
}