package com.blocker.engine.wordfilter.engine

import com.blocker.core.common.model.WordEntry
import com.blocker.engine.wordfilter.matcher.WordEntryMatch
import com.blocker.engine.wordfilter.matcher.WordEntryMatcher
import com.blocker.engine.wordfilter.matcher.WordEntryMatchValidator

class WordEntryDetectionEngine {

    fun detect(
        text: String,
        entries: List<WordEntry>
    ): List<WordEntryMatch> {

        if (text.isBlank() || entries.isEmpty()) {
            return emptyList()
        }

        val matches = WordEntryMatcher.findMatches(
            text = text,
            entries = entries
        )

        val validMatches =
            WordEntryMatchValidator.removeInvalidMatches(
                text = text,
                matches = matches
            )

        return WordEntryMatchValidator.removeOverlappingMatches(
            validMatches
        )
    }

    fun containsBlockedWord(
        text: String,
        entries: List<WordEntry>
    ): Boolean {
        return detect(text, entries).isNotEmpty()
    }

    fun detectFirst(
        text: String,
        entries: List<WordEntry>
    ): WordEntryMatch? {
        return detect(text, entries).firstOrNull()
    }
}
