package com.blocker.core.common

/**
 * Result of checking a text against configured words.
 */
data class WordMatchResult(

    val matched: Boolean,

    val matchedWord: String? = null,

    val normalizedWord: String? = null,

    val matchedText: String? = null,

    val startIndex: Int = -1,

    val endIndex: Int = -1,

    val confidence: Float = 0f,

    val reason: MatchReason = MatchReason.NO_MATCH
) {

    companion object {

        fun noMatch(): WordMatchResult {
            return WordMatchResult(
                matched = false,
                reason = MatchReason.NO_MATCH
            )
        }

        fun match(
            word: String,
            normalizedWord: String,
            matchedText: String,
            startIndex: Int,
            endIndex: Int
        ): WordMatchResult {
            return WordMatchResult(
                matched = true,
                matchedWord = word,
                normalizedWord = normalizedWord,
                matchedText = matchedText,
                startIndex = startIndex,
                endIndex = endIndex,
                confidence = 1f,
                reason = MatchReason.EXACT_MATCH
            )
        }
    }
}

/**
 * Explains why a word was or was not detected.
 */
enum class MatchReason {

    NO_MATCH,

    EXACT_MATCH,

    CASE_INSENSITIVE_MATCH,

    NORMALIZED_MATCH,

    PARTIAL_MATCH,

    INVALID_SEARCH_TERM,

    LOW_CONFIDENCE
}
