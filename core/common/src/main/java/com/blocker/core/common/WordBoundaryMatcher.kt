package com.blocker.core.common

/**
 * Matches complete words and phrases instead of
 * accidentally matching them inside longer words.
 *
 * Examples:
 * "bad" matches "this is bad"
 * "bad" does not match "badge"
 */
object WordBoundaryMatcher {

    private val wordCharacterRegex =
        Regex("[\\p{L}\\p{N}_]")

    /**
     * Finds the first complete occurrence of a term.
     *
     * The supplied text and term must already be normalized.
     */
    fun find(
        text: String,
        term: String
    ): IntRange? {

        if (text.isBlank() || term.isBlank()) {
            return null
        }

        var searchFrom = 0

        while (searchFrom <= text.length - term.length) {

            val start = text.indexOf(
                string = term,
                startIndex = searchFrom
            )

            if (start == -1) {
                return null
            }

            val endExclusive = start + term.length

            val leftBoundaryValid =
                start == 0 ||
                    !isWordCharacter(text[start - 1])

            val rightBoundaryValid =
                endExclusive >= text.length ||
                    !isWordCharacter(text[endExclusive])

            if (leftBoundaryValid && rightBoundaryValid) {
                return start until endExclusive
            }

            searchFrom = start + 1
        }

        return null
    }

    /**
     * Returns true when the character belongs to a word.
     */
    private fun isWordCharacter(character: Char): Boolean {
        return wordCharacterRegex.matches(character.toString())
    }

    /**
     * Checks whether a complete term exists in the text.
     */
    fun contains(
        text: String,
        term: String
    ): Boolean {
        return find(text, term) != null
    }
}
