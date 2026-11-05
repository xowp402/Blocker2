package com.blocker.core.data.usecase

import com.blocker.core.common.model.WordEntry
import com.blocker.core.common.validation.WordEntryValidator
import com.blocker.core.data.repository.WordEntryRepository

class WordEntryUseCases(
    private val repository: WordEntryRepository
) {

    fun observeAll() = repository.observeAll()

    fun observeEnabled() = repository.observeEnabled()

    fun search(query: String) =
        repository.observeSearch(query.trim())

    suspend fun getById(id: Long) =
        repository.getById(id)

    suspend fun add(entry: WordEntry): Result<Long> {
        val validation = WordEntryValidator.validate(entry)

        if (validation !is com.blocker.core.common.model.ValidationResult.Valid) {
            return Result.failure(
                IllegalArgumentException("Invalid word entry: $validation")
            )
        }

        if (repository.exists(entry.normalizedWord)) {
            return Result.failure(
                IllegalArgumentException("This word already exists.")
            )
        }

        return runCatching {
            repository.add(entry)
        }
    }

    suspend fun update(entry: WordEntry): Result<Unit> {
        val validation = WordEntryValidator.validate(entry)

        if (validation !is com.blocker.core.common.model.ValidationResult.Valid) {
            return Result.failure(
                IllegalArgumentException("Invalid word entry: $validation")
            )
        }

        return runCatching {
            repository.update(entry)
        }
    }

    suspend fun delete(id: Long) =
        repository.deleteById(id)

    suspend fun deleteAll() =
        repository.deleteAll()

    suspend fun setEnabled(
        id: Long,
        enabled: Boolean
    ) = repository.setEnabled(id, enabled)

    suspend fun recordDetection(
        id: Long,
        timestamp: Long = System.currentTimeMillis()
    ) = repository.incrementDetectionCount(id, timestamp)

    suspend fun deleteMany(ids: List<Long>) =
        repository.deleteMany(ids)
}
