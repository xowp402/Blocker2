package com.blocker.core.data.service

import com.blocker.core.common.factory.WordEntryFactory
import com.blocker.core.common.model.WordEntryImportResult
import com.blocker.core.data.repository.WordEntryRepository

class WordEntryImportService(
    private val repository: WordEntryRepository
) {

    suspend fun importWords(
        text: String,
        categoryId: String? = null
    ): WordEntryImportResult {

        val lines = text
            .lines()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }

        var importedCount = 0
        var duplicateCount = 0

        val importedWords = mutableListOf<String>()
        val duplicateWords = mutableListOf<String>()
        val invalidWords = mutableListOf<String>()

        for (word in lines) {
            try {
                val entry = WordEntryFactory.create(
                    word = word,
                    categoryId = categoryId
                )

                if (repository.exists(entry.normalizedWord)) {
                    duplicateCount++
                    duplicateWords.add(word)
                    continue
                }

                repository.add(entry)
                importedCount++
                importedWords.add(word)

            } catch (_: IllegalArgumentException) {
                invalidWords.add(word)
            } catch (_: Exception) {
                invalidWords.add(word)
            }
        }

        return WordEntryImportResult(
            totalLines = lines.size,
            importedCount = importedCount,
            duplicateCount = duplicateCount,
            invalidCount = invalidWords.size,
            importedWords = importedWords,
            duplicateWords = duplicateWords,
            invalidWords = invalidWords
        )
    }
}
