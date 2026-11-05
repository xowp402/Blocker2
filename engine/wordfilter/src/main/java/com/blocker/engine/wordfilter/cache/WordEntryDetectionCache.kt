package com.blocker.engine.wordfilter.cache

import com.blocker.core.common.model.WordEntry
import java.util.concurrent.ConcurrentHashMap

class WordEntryDetectionCache(
    private val maxEntries: Int = 500
) {

    private val cache =
        ConcurrentHashMap<String, List<WordEntry>>()

    fun get(key: String): List<WordEntry>? {
        if (key.isBlank()) return null
        return cache[key]
    }

    fun put(
        key: String,
        entries: List<WordEntry>
    ) {
        if (key.isBlank()) return

        if (cache.size >= maxEntries && !cache.containsKey(key)) {
            val oldestKey = cache.keys.firstOrNull()
            if (oldestKey != null) {
                cache.remove(oldestKey)
            }
        }

        cache[key] = entries.toList()
    }

    fun invalidate(key: String) {
        cache.remove(key)
    }

    fun clear() {
        cache.clear()
    }

    fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    val size: Int
        get() = cache.size
}
