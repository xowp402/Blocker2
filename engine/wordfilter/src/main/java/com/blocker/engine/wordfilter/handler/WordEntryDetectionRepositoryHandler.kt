package com.blocker.engine.wordfilter.handler

import com.blocker.core.data.repository.WordEntryRepository
import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

class WordEntryDetectionRepositoryHandler(
    private val repository: WordEntryRepository
) : WordEntryDetectionEventHandler {

    override suspend fun handle(
        event: WordEntryDetectionEvent
    ) {
        if (!event.isValid) return

        val entry = repository.getByNormalizedWord(
            normalize(event.matchedWord)
        ) ?: return

        if (!entry.isEnabled) return

        repository.incrementDetectionCount(
            id = entry.id,
            detectedAt = event.detectedAt
        )
    }

    private fun normalize(value: String): String {
        return value
            .trim()
            .replace('\u064A', '\u06CC')
            .replace('\u0643', '\u06A9')
            .replace("\u200C", "")
            .lowercase()
    }
}
