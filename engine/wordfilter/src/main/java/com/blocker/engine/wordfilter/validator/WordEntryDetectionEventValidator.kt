package com.blocker.engine.wordfilter.validator

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

object WordEntryDetectionEventValidator {

    fun validate(
        event: WordEntryDetectionEvent
    ): Boolean {

        if (event.eventId.isBlank()) return false

        if (event.packageName.isBlank()) return false

        if (event.matchedWord.isBlank()) return false

        if (event.detectedAt <= 0L) return false

        if (event.detectionCount <= 0) return false

        if (event.packageName == "com.blocker.app") {
            return false
        }

        return true
    }

    fun validateAll(
        events: List<WordEntryDetectionEvent>
    ): List<WordEntryDetectionEvent> {

        return events.filter(::validate)
    }
}
