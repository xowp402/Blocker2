package com.blocker.engine.wordfilter.matcher

object WordEntryMatchValidator {

    fun isValid(
        text: String,
        match: WordEntryMatch
    ): Boolean {

        if (text.isBlank()) return false

        if (match.entryId <= 0L) return false

        if (match.matchedWord.isBlank()) return false

        if (match.startIndex < 0) return false

        if (match.endIndex <= match.startIndex) return false

        if (match.endIndex > text.length) return false

        return true
    }

    fun removeInvalidMatches(
        text: String,
        matches: List<WordEntryMatch>
    ): List<WordEntryMatch> {

        return matches.filter { match ->
            isValid(text, match)
        }
    }

    fun removeOverlappingMatches(
        matches: List<WordEntryMatch>
    ): List<WordEntryMatch> {

        val sorted = matches.sortedWith(
            compareBy<WordEntryMatch> { it.startIndex }
                .thenByDescending { it.endIndex }
        )

        val result = mutableListOf<WordEntryMatch>()
        var lastEnd = -1

        for (match in sorted) {
            if (match.startIndex >= lastEnd) {
                result.add(match)
                lastEnd = match.endIndex
            }
        }

        return result
    }
}
