package com.blocker.engine.wordfilter.engine

import com.blocker.core.common.model.WordEntry
import com.blocker.engine.wordfilter.model.WordEntryDetectionResult

enum class DetectionAction {
    BLOCK,
    WARN_ONLY,
    IGNORE
}

data class WordEntryDetectionDecision(
    val action: DetectionAction,
    val matchedEntries: List<WordEntry>,
    val reason: String
)

class WordEntryDetectionPolicy {

    fun evaluate(
        result: WordEntryDetectionResult,
        entries: List<WordEntry>,
        monitoringEnabled: Boolean = true
    ): WordEntryDetectionDecision {

        if (!monitoringEnabled) {
            return decision(
                DetectionAction.IGNORE,
                emptyList(),
                "Word monitoring is disabled."
            )
        }

        if (!result.detected || result.matches.isEmpty()) {
            return decision(
                DetectionAction.IGNORE,
                emptyList(),
                "No configured word was detected."
            )
        }

        val matchedIds = result.matches
            .map { it.entryId }
            .toSet()

        val matchedEntries = entries.filter {
            it.isEnabled && it.id in matchedIds
        }

        if (matchedEntries.isEmpty()) {
            return decision(
                DetectionAction.IGNORE,
                emptyList(),
                "No enabled matching rule was found."
            )
        }

        return decision(
            DetectionAction.BLOCK,
            matchedEntries,
            "An enabled configured word was detected."
        )
    }

    private fun decision(
        action: DetectionAction,
        entries: List<WordEntry>,
        reason: String
    ) = WordEntryDetectionDecision(
        action = action,
        matchedEntries = entries,
        reason = reason
    )
}
