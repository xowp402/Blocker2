package com.blocker.engine.wordfilter.model

data class WordEntryDetectionEvent(
    val eventId: String,
    val packageName: String,
    val matchedWord: String,
    val detectedAt: Long = System.currentTimeMillis(),
    val detectionCount: Int = 1
) {
    val isValid: Boolean
        get() = eventId.isNotBlank() &&
            packageName.isNotBlank() &&
            matchedWord.isNotBlank() &&
            detectedAt > 0L &&
            detectionCount > 0
}
