package com.blocker.core.data.service

import com.blocker.core.data.repository.WordEntryRepository

class WordEntryBulkActionService(
    private val repository: WordEntryRepository
) {

    suspend fun enableMany(ids: List<Long>) {
        ids.distinct().forEach { id ->
            repository.setEnabled(id, true)
        }
    }

    suspend fun disableMany(ids: List<Long>) {
        ids.distinct().forEach { id ->
            repository.setEnabled(id, false)
        }
    }

    suspend fun deleteMany(ids: List<Long>) {
        if (ids.isEmpty()) return

        repository.deleteMany(ids.distinct())
    }

    suspend fun deleteAll() {
        repository.deleteAll()
    }
}
