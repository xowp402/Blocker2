package com.blocker.engine.wordfilter.handler

import com.blocker.engine.wordfilter.logging.WordEntryDetectionLogger
import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

class WordEntryDetectionLoggingHandler(
    private val logger: WordEntryDetectionLogger =
        WordEntryDetectionLogger()
) : WordEntryDetectionEventHandler {

    override suspend fun handle(
        event: WordEntryDetectionEvent
    ) {
        if (!event.isValid) return

        logger.detection(
            packageName = event.packageName,
            matchedWord = event.matchedWord
        )
    }
}
