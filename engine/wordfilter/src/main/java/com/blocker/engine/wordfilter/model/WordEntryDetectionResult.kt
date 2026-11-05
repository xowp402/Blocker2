package com.blocker.engine.wordfilter.model

import com.blocker.engine.wordfilter.matcher.WordEntryMatch

data class WordEntryDetectionResult(
    val detected: Boolean,
    val matches: List<WordEntryMatch> = emptyList(),
    val detectedAt: Long = System.currentTimeMillis()
) {
    val matchCount: Int
        get() = matches.size

    val firstMatch: WordEntryMatch?
        get() = matches.firstOrNull()

    companion object {

        fun detected(
            matches: List<WordEntryMatch>
        ): WordEntryDetectionResult {
            return WordEntryDetectionResult(
                detected = matches.isNotEmpty(),
                matches = matches
            )
        }

        fun notDetected(): WordEntryDetectionResult {
            return WordEntryDetectionResult(
                detected = false
            )
        }
    }
}
