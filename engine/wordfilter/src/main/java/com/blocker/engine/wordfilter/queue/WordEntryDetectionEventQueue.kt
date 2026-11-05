package com.blocker.engine.wordfilter.queue

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent
import kotlinx.coroutines.channels.Channel

class WordEntryDetectionEventQueue(
    capacity: Int = 100
) {
    private val channel = Channel<WordEntryDetectionEvent>(
        capacity = capacity.coerceAtLeast(1)
    )

    suspend fun enqueue(
        event: WordEntryDetectionEvent
    ): Boolean {
        if (!event.isValid) return false

        return channel.trySend(event).isSuccess
    }

    suspend fun dequeue(): WordEntryDetectionEvent? {
        return channel.receiveCatching().getOrNull()
    }

    fun close() {
        channel.close()
    }

    val isClosed: Boolean
        get() = channel.isClosedForReceive
}
