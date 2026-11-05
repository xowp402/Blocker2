package com.blocker.engine.wordfilter.model

import com.blocker.engine.wordfilter.engine.WordEntryDetectionDecision

data class WordEntryDetectionResponse(
    val requestId: String,
    val packageName: String,
    val detectionResult: WordEntryDetectionResult,
    val decision: WordEntryDetectionDecision,
    val processedAt: Long = System.currentTimeMillis(),
    val processingDurationMillis: Long = 0L
) {
    val shouldBlock: Boolean
        get() = decision.action ==
            com.blocker.engine.wordfilter.engine.DetectionAction.BLOCK

    val hasDetection: Boolean
        get() = detectionResult.detected

    val matchedWords: List<String>
        get() = decision.matchedEntries.map { it.word }

    val isValid: Boolean
        get() = requestId.isNotBlank() &&
            packageName.isNotBlank() &&
            processedAt > 0L &&
            processingDurationMillis >= 0L
}
