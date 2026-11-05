package com.blocker.core.data.service

import com.blocker.core.common.model.WordEntry
import com.blocker.core.data.repository.WordEntryRepository
import kotlinx.coroutines.flow.first

class WordEntrySearchService(
    private val repository: WordEntryRepository
) {

    suspend fun search(query: String): List<WordEntry> {
        val cleanQuery = query.trim()

        if (cleanQuery.isBlank()) {
            return repository.observeAll().first()
        }

        return repository.observeSearch(cleanQuery).first()
    }

    suspend fun findById(id: Long): WordEntry? {
        if (id <= 0L) return null

        return repository.getById(id)
    }

    suspend fun findByNormalizedWord(
        normalizedWord: String
    ): WordEntry? {
        val cleanWord = normalizedWord.trim()

        if (cleanWord.isBlank()) return null

        return repository.getByNormalizedWord(cleanWord)
    }
}
