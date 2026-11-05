package com.blocker.core.data.repository

import com.blocker.core.common.model.WordEntry
import kotlinx.coroutines.flow.Flow

interface WordEntryRepository {

    fun observeAll(): Flow<List<WordEntry>>

    fun observeEnabled(): Flow<List<WordEntry>>

    fun observeByCategory(categoryId: String): Flow<List<WordEntry>>

    fun observeSearch(query: String): Flow<List<WordEntry>>

    suspend fun getById(id: Long): WordEntry?

    suspend fun getByNormalizedWord(normalizedWord: String): WordEntry?

    suspend fun add(entry: WordEntry): Long

    suspend fun update(entry: WordEntry)

    suspend fun deleteById(id: Long)

    suspend fun deleteAll()

    suspend fun setEnabled(
        id: Long,
        enabled: Boolean
    )

    suspend fun incrementDetectionCount(
        id: Long,
        detectedAt: Long
    )

    suspend fun exists(normalizedWord: String): Boolean

    suspend fun deleteMany(ids: List<Long>)
}
