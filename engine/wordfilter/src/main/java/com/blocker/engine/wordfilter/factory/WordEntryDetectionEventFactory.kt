package com.blocker.engine.wordfilter.factory

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent
import java.util.UUID

object WordEntryDetectionEventFactory {

    fun create(
        packageName: String,
        matchedWord: String,
        detectedAt: Long = System.currentTimeMillis()
    ): WordEntryDetectionEvent {

        require(packageName.isNotBlank()) {
            "Package name cannot be empty."
        }

        require(matchedWord.isNotBlank()) {
            "Matched word cannot be empty."
        }

        require(detectedAt > 0L) {
            "Invalid detection timestamp."
        }

        return WordEntryDetectionEvent(
            eventId = UUID.randomUUID().toString(),
            packageName = packageName,
            matchedWord = matchedWord,
            detectedAt = detectedAt,
            detectionCount = 1
        )
    }
}
