package com.blocker.core.data.service

import com.blocker.core.common.util.TextNormalizer
import com.blocker.core.data.repository.WordEntryRepository

class WordEntryDuplicateChecker(
    private val repository: WordEntryRepository
) {

    suspend fun isDuplicate(word: String): Boolean {
        val normalized = TextNormalizer.normalize(word)

        if (normalized.isBlank()) {
            return false
        }

        return repository.exists(normalized)
    }

    suspend fun findDuplicate(word: String) =
        repository.getByNormalizedWord(
            TextNormalizer.normalize(word)
        )
}
