package com.blocker.engine.wordfilter.queue

import com.blocker.engine.wordfilter.model.WordEntryDetectionEvent
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WordEntryDetectionEventQueueConsumer(
    private val queue: WordEntryDetectionEventQueue,
    private val scope: CoroutineScope,
    private val onEvent: suspend (WordEntryDetectionEvent) -> Unit,
    private val onError: (Throwable) -> Unit = {}
) {

    private var consumerJob: Job? = null

    @Synchronized
    fun start() {
        if (consumerJob?.isActive == true) return

        consumerJob = scope.launch {
            while (isActive) {
                val event = queue.dequeue() ?: break

                try {
                    onEvent(event)
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Throwable) {
                    onError(exception)
                }
            }
        }
    }

    @Synchronized
    fun stop() {
        consumerJob?.cancel()
        consumerJob = null
    }

    val isRunning: Boolean
        get() = consumerJob?.isActive == true
}
