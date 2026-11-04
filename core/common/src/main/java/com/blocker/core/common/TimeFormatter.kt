package com.blocker.core.common

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Formats durations and timestamps for Blocker's UI.
 */
object TimeFormatter {

    /**
     * Formats seconds as HH:MM:SS.
     */
    fun formatDuration(
        seconds: Long,
        usePersianDigits: Boolean = true
    ): String {

        if (seconds == Long.MAX_VALUE) {
            return "نامحدود"
        }

        val safeSeconds = seconds.coerceAtLeast(0L)

        val hours = TimeUnit.SECONDS.toHours(safeSeconds)

        val minutes = TimeUnit.SECONDS.toMinutes(safeSeconds) % 60

        val remainingSeconds = safeSeconds % 60

        val formatted = if (hours > 0) {
            String.format(
                Locale.ROOT,
                "%02d:%02d:%02d",
                hours,
                minutes,
                remainingSeconds
            )
        } else {
            String.format(
                Locale.ROOT,
                "%02d:%02d",
                minutes,
                remainingSeconds
            )
        }

        return if (usePersianDigits) {
            toPersianDigits(formatted)
        } else {
            formatted
        }
    }

    /**
     * Formats a timestamp as a readable date and time.
     */
    fun formatTimestamp(
        timestamp: Long,
        usePersianDigits: Boolean = true
    ): String {

        val formatter = java.text.SimpleDateFormat(
            "yyyy/MM/dd HH:mm",
            Locale.getDefault()
        )

        val formatted = formatter.format(
            java.util.Date(timestamp)
        )

        return if (usePersianDigits) {
            toPersianDigits(formatted)
        } else {
            formatted
        }
    }

    /**
     * Converts English digits into Persian digits.
     */
    fun toPersianDigits(value: String): String {

        val digits = mapOf(
            '0' to '۰',
            '1' to '۱',
            '2' to '۲',
            '3' to '۳',
            '4' to '۴',
            '5' to '۵',
            '6' to '۶',
            '7' to '۷',
            '8' to '۸',
            '9' to '۹'
        )

        return buildString {
            value.forEach { character ->
                append(digits[character] ?: character)
            }
        }
    }

    /**
     * Converts Persian digits into English digits.
     */
    fun toEnglishDigits(value: String): String {

        val digits = mapOf(
            '۰' to '0',
            '۱' to '1',
            '۲' to '2',
            '۳' to '3',
            '۴' to '4',
            '۵' to '5',
            '۶' to '6',
            '۷' to '7',
            '۸' to '8',
            '۹' to '9'
        )

        return buildString {
            value.forEach { character ->
                append(digits[character] ?: character)
            }
        }
    }
}
