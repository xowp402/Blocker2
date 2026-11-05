package com.blocker.engine.wordfilter.handler

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

fun interface WordEntryDetectionEventHandler {

    suspend fun handle(
        event: WordEntryDetectionEvent
    )
}
