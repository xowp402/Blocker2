package com.blocker.engine.wordfilter.engine

import com.blocker.engine.wordfilter.model.WordEntryDetectionRequest
import com.blocker.engine.wordfilter.model.WordEntryDetectionResponse
import java.util.UUID

class WordEntryDetectionOrchestrator(
    private val pipeline: WordEntryDetectionPipeline =
        WordEntryDetectionPipeline()
) {

    fun execute(
        request: WordEntryDetectionRequest
    ): WordEntryDetectionResponse {

        val startTime = System.nanoTime()

        val result = if (request.isValid) {
            pipeline.execute(
                text = request.visibleText,
                entries = request.enabledEntries,
                monitoringEnabled = request.monitoringEnabled
            )
        } else {
            pipeline.execute(
                text = "",
                entries = emptyList(),
                monitoringEnabled = false
            )
        }

        val durationMillis =
            (System.nanoTime() - startTime) / 1_000_000L

        return WordEntryDetectionResponse(
            requestId = UUID.randomUUID().toString(),
            packageName = request.packageName,
            detectionResult = result.detection,
            decision = result.decision,
            processedAt = System.currentTimeMillis(),
            processingDurationMillis = durationMillis
        )
    }
}
