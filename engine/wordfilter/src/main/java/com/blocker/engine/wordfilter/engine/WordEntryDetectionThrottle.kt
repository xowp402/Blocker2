package com.blocker.engine.wordfilter.engine

import java.util.concurrent.ConcurrentHashMap

class WordEntryDetectionThrottle(
    private val cooldownMillis: Long = 1_000L
) {

    private val lastDetectionTimes =
        ConcurrentHashMap<String, Long>()

    fun shouldProcess(
        packageName: String,
        text: String,
        currentTimeMillis: Long = System.currentTimeMillis()
    ): Boolean {

        if (packageName.isBlank() || text.isBlank()) {
            return false
        }

        val key = "$packageName:${text.hashCode()}"
        val previousTime = lastDetectionTimes[key]

        if (
            previousTime != null &&
            currentTimeMillis - previousTime < cooldownMillis
        ) {
            return false
        }

        lastDetectionTimes[key] = currentTimeMillis
        cleanup(currentTimeMillis)

        return true
    }

    fun clearPackage(packageName: String) {
        lastDetectionTimes.keys.removeAll { key ->
            key.startsWith("$packageName:")
        }
    }

    fun clearAll() {
        lastDetectionTimes.clear()
    }

    private fun cleanup(currentTimeMillis: Long) {
        lastDetectionTimes.entries.removeAll { entry ->
            currentTimeMillis - entry.value > cooldownMillis * 10
        }
    }
}
