package com.blocker.core.data.service

import com.blocker.core.common.model.WordEntry
import com.blocker.core.data.repository.WordEntryRepository

class WordEntryDetectionService(
    private val repository: WordEntryRepository
) {

    suspend fun getActiveWords(): List<WordEntry> {
        return repository.observeEnabled()
            .first()
    }

    suspend fun recordDetection(
        entry: WordEntry,
        detectedAt: Long = System.currentTimeMillis()
    ) {
        repository.incrementDetectionCount(
            id = entry.id,
            detectedAt = detectedAt
        )
    }
}
