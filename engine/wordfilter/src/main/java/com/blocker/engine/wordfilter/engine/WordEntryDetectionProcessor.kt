package com.blocker.engine.wordfilter.engine

import com.blocker.core.common.model.WordEntry
import com.blocker.engine.wordfilter.model.WordEntryDetectionResult

class WordEntryDetectionProcessor(
    private val engine: WordEntryDetectionEngine =
        WordEntryDetectionEngine()
) {

    fun process(
        text: String,
        entries: List<WordEntry>
    ): WordEntryDetectionResult {

        if (text.isBlank() || entries.isEmpty()) {
            return WordEntryDetectionResult.notDetected()
        }

        val matches = engine.detect(
            text = text,
            entries = entries
        )

        return if (matches.isEmpty()) {
            WordEntryDetectionResult.notDetected()
        } else {
            WordEntryDetectionResult.detected(matches)
        }
    }
}
