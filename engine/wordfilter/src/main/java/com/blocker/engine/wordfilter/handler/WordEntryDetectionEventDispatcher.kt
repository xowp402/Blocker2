package com.blocker.engine.wordfilter.handler

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent

class WordEntryDetectionEventDispatcher(
    private val handlers: List<WordEntryDetectionEventHandler>
) {

    suspend fun dispatch(
        event: WordEntryDetectionEvent
    ) {
        if (!event.isValid) return

        handlers.forEach { handler ->
            runCatching {
                handler.handle(event)
            }
        }
    }

    suspend fun dispatchAll(
        events: List<WordEntryDetectionEvent>
    ) {
        events
            .filter { it.isValid }
            .forEach { event ->
                dispatch(event)
            }
    }
}
