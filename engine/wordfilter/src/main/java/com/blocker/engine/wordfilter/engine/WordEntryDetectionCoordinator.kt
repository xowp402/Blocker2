package com.blocker.engine.wordfilter.engine

import com.blocker.core.common.model.WordEntry
import com.blocker.engine.wordfilter.model.WordEntryDetectionResult
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class WordEntryDetectionCoordinator(
    private val processor: WordEntryDetectionProcessor =
        WordEntryDetectionProcessor()
) {

    private val mutex = Mutex()

    suspend fun process(
        text: String,
        entries: List<WordEntry>
    ): WordEntryDetectionResult {

        if (text.isBlank() || entries.isEmpty()) {
            return WordEntryDetectionResult.notDetected()
        }

        return mutex.withLock {
            processor.process(
                text = text,
                entries = entries
            )
        }
    }
}
