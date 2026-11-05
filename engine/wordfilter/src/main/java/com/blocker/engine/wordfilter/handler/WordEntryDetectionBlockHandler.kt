package com.blocker.engine.wordfilter.handler

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

fun interface WordDetectionBlockAction {

    suspend fun block(
        packageName: String,
        matchedWord: String,
        detectedAt: Long
    )
}

class WordEntryDetectionBlockHandler(
    private val blockAction: WordDetectionBlockAction
) : WordEntryDetectionEventHandler {

    override suspend fun handle(
        event: WordEntryDetectionEvent
    ) {
        if (!event.isValid) return

        blockAction.block(
            packageName = event.packageName,
            matchedWord = event.matchedWord,
            detectedAt = event.detectedAt
        )
    }
}
