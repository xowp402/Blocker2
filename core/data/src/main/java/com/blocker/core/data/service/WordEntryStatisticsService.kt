package com.blocker.core.data.service

import com.blocker.core.data.repository.WordEntryRepository
import kotlinx.coroutines.flow.first

class WordEntryStatisticsService(
    private val repository: WordEntryRepository
) {

    suspend fun getStatistics(): WordEntryStatistics {
        val entries = repository.observeAll().first()

        return WordEntryStatistics(
            totalWords = entries.size,
            enabledWords = entries.count { it.isEnabled },
            disabledWords = entries.count { !it.isEnabled },
            totalDetections = entries.sumOf { it.detectionCount },
            categoriesCount = entries
                .mapNotNull { it.categoryId }
                .distinct()
                .size,
            mostDetectedWord = entries
                .maxByOrNull { it.detectionCount }
                ?.takeIf { it.detectionCount > 0 }
                ?.word,
            lastDetectedWord = entries
                .filter { it.lastDetectedAt != null }
                .maxByOrNull { it.lastDetectedAt!! }
                ?.word
        )
    }
}

data class WordEntryStatistics(
    val totalWords: Int,
    val enabledWords: Int,
    val disabledWords: Int,
    val totalDetections: Long,
    val categoriesCount: Int,
    val mostDetectedWord: String?,
    val lastDetectedWord: String?
)
