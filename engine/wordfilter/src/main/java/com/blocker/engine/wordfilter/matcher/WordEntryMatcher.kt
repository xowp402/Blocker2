package com.blocker.engine.wordfilter.matcher

import com.blocker.core.common.model.WordEntry
import com.blocker.core.common.util.TextNormalizer

data class WordEntryMatch(
    val entryId: Long,
    val matchedWord: String,
    val startIndex: Int,
    val endIndex: Int
)

object WordEntryMatcher {

    fun findMatches(
        text: String,
        entries: List<WordEntry>
    ): List<WordEntryMatch> {

        if (text.isBlank() || entries.isEmpty()) {
            return emptyList()
        }

        val normalizedText = TextNormalizer.normalize(text)
        if (normalizedText.isBlank()) {
            return emptyList()
        }

        val matches = mutableListOf<WordEntryMatch>()

        entries
            .asSequence()
            .filter { it.isEnabled }
            .filter { it.normalizedWord.isNotBlank() }
            .forEach { entry ->

                val normalizedWord =
                    TextNormalizer.normalize(entry.normalizedWord)

                if (normalizedWord.isBlank()) return@forEach

                var searchFrom = 0

                while (searchFrom < normalizedText.length) {

                    val index = normalizedText.indexOf(
                        string = normalizedWord,
                        startIndex = searchFrom,
                        ignoreCase = true
                    )

                    if (index < 0) break

                    val endIndex = index + normalizedWord.length

                    matches.add(
                        WordEntryMatch(
                            entryId = entry.id,
                            matchedWord = entry.word,
                            startIndex = index,
                            endIndex = endIndex
                        )
                    )

                    searchFrom = index + 1
                }
            }

        return matches
            .distinctBy {
                Triple(it.entryId, it.startIndex, it.endIndex)
            }
            .sortedBy { it.startIndex }
    }

    fun containsBlockedWord(
        text: String,
        entries: List<WordEntry>
    ): Boolean {
        return findMatches(text, entries).isNotEmpty()
    }

    fun findFirstMatch(
        text: String,
        entries: List<WordEntry>
    ): WordEntryMatch? {
        return findMatches(text, entries).firstOrNull()
    }
}
