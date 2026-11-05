package com.blocker.engine.wordfilter.mapper

import com.blocker.engine.wordfilter.factory.WordEntryDetectionEventFactory
import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent
import com.blocker.engine.wordfilter.model.WordEntryDetectionResponse

object WordEntryDetectionEventMapper {

    fun toEvents(
        response: WordEntryDetectionResponse
    ): List<WordEntryDetectionEvent> {

        if (!response.isValid || !response.shouldBlock) {
            return emptyList()
        }

        return response.decision.matchedEntries
            .distinctBy { it.id }
            .mapNotNull { entry ->

                runCatching {
                    WordEntryDetectionEventFactory.create(
                        packageName = response.packageName,
                        matchedWord = entry.word,
                        detectedAt = response.detectionResult.detectedAt
                    )
                }.getOrNull()
            }
    }
}
