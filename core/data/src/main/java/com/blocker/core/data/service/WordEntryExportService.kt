package com.blocker.core.data.service

import com.blocker.core.data.repository.WordEntryRepository
import kotlinx.coroutines.flow.first

class WordEntryExportService(
    private val repository: WordEntryRepository
) {

    suspend fun exportAll(): String {
        return repository.observeAll()
            .first()
            .joinToString(separator = "\n") { entry ->
                entry.word
            }
    }

    suspend fun exportEnabled(): String {
        return repository.observeEnabled()
            .first()
            .joinToString(separator = "\n") { entry ->
                entry.word
            }
    }

    suspend fun exportByCategory(categoryId: String): String {
        return repository.observeByCategory(categoryId)
            .first()
            .joinToString(separator = "\n") { entry ->
                entry.word
            }
    }
}
