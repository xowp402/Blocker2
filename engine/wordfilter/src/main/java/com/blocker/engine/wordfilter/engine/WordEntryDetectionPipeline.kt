package com.blocker.engine.wordfilter.engine

import com.blocker.core.common.model.WordEntry
import com.blocker.engine.wordfilter.model.WordEntryDetectionResult

class WordEntryDetectionPipeline(
    private val processor: WordEntryDetectionProcessor =
        WordEntryDetectionProcessor(),
    private val policy: WordEntryDetectionPolicy =
        WordEntryDetectionPolicy()
) {

    fun execute(
        text: String,
        entries: List<WordEntry>,
        monitoringEnabled: Boolean = true
    ): PipelineResult {

        if (!monitoringEnabled) {
            return PipelineResult(
                detection = WordEntryDetectionResult.notDetected(),
                decision = WordEntryDetectionDecision(
                    action = DetectionAction.IGNORE,
                    matchedEntries = emptyList(),
                    reason = "Word monitoring is disabled."
                )
            )
        }

        val enabledEntries = entries.filter { it.isEnabled }

        val detection = processor.process(
            text = text,
            entries = enabledEntries
        )

        val decision = policy.evaluate(
            result = detection,
            entries = enabledEntries,
            monitoringEnabled = monitoringEnabled
        )

        return PipelineResult(
            detection = detection,
            decision = decision
        )
    }
}

data class PipelineResult(
    val detection: WordEntryDetectionResult,
    val decision: WordEntryDetectionDecision
) {
    val shouldBlock: Boolean
        get() = decision.action == DetectionAction.BLOCK

    val matchedEntries: List<WordEntry>
        get() = decision.matchedEntries
}
