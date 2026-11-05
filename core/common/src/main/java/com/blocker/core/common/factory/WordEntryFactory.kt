package com.blocker.core.common.factory

import com.blocker.core.common.model.WordEntry
import com.blocker.core.common.util.TextNormalizer

object WordEntryFactory {

    fun create(
        word: String,
        categoryId: String? = null,
        isEnabled: Boolean = true
    ): WordEntry {
        val now = System.currentTimeMillis()
        val cleanWord = word.trim()

        require(cleanWord.isNotBlank()) {
            "Word cannot be empty."
        }

        val normalized = TextNormalizer.normalize(cleanWord)

        require(normalized.isNotBlank()) {
            "Normalized word cannot be empty."
        }

        return WordEntry(
            id = 0L,
            word = cleanWord,
            normalizedWord = normalized,
            categoryId = categoryId,
            isEnabled = isEnabled,
            detectionCount = 0L,
            lastDetectedAt = null,
            createdAt = now,
            updatedAt = now
        )
    }
}
