package com.blocker.core.data.repository

import com.blocker.core.common.model.WordEntry
import com.blocker.core.data.local.dao.WordEntryDao
import com.blocker.core.data.local.mapper.WordEntryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WordEntryRepositoryImpl(
    private val dao: WordEntryDao
) : WordEntryRepository {

    override fun observeAll(): Flow<List<WordEntry>> {
        return dao.observeAll().map(WordEntryMapper::toDomainList)
    }

    override fun observeEnabled(): Flow<List<WordEntry>> {
        return dao.observeEnabled().map(WordEntryMapper::toDomainList)
    }

    override fun observeByCategory(
        categoryId: String
    ): Flow<List<WordEntry>> {
        return dao.observeByCategory(categoryId)
            .map(WordEntryMapper::toDomainList)
    }

    override fun observeSearch(
        query: String
    ): Flow<List<WordEntry>> {
        return dao.search(query)
            .map(WordEntryMapper::toDomainList)
    }

    override suspend fun getById(id: Long): WordEntry? {
        return dao.getById(id)?.let(WordEntryMapper::toDomain)
    }

    override suspend fun getByNormalizedWord(
        normalizedWord: String
    ): WordEntry? {
        return dao.getByNormalizedWord(normalizedWord)
            ?.let(WordEntryMapper::toDomain)
    }

    override suspend fun add(entry: WordEntry): Long {
        return dao.insert(WordEntryMapper.toEntity(entry))
    }

    override suspend fun update(entry: WordEntry) {
        dao.update(WordEntryMapper.toEntity(entry))
    }

    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override suspend fun setEnabled(
        id: Long,
        enabled: Boolean
    ) {
        dao.setEnabled(id, enabled)
    }

    override suspend fun incrementDetectionCount(
        id: Long,
        detectedAt: Long
    ) {
        dao.incrementDetectionCount(id, detectedAt)
    }

    override suspend fun exists(normalizedWord: String): Boolean {
        return dao.exists(normalizedWord)
    }

    override suspend fun deleteMany(ids: List<Long>) {
        dao.deleteMany(ids)
    }
}
