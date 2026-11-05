package com.blocker.core.data.repository

import com.blocker.core.common.AppBulkAction
import com.blocker.core.data.local.dao.AppBulkActionDao
import com.blocker.core.data.mapper.AppBulkActionMapper.toDomain
import com.blocker.core.data.mapper.AppBulkActionMapper.toDomainList
import com.blocker.core.data.mapper.AppBulkActionMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * پیاده‌سازی مخزن عملیات گروهی برنامه‌ها.
 */
class AppBulkActionRepositoryImpl(
    private val dao: AppBulkActionDao
) : AppBulkActionRepository {

    override fun observeAll(): Flow<List<AppBulkAction>> {
        return dao.observeAll().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getById(
        id: String
    ): AppBulkAction? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun insert(
        action: AppBulkAction
    ) {
        dao.insert(action.toEntity())
    }

    override suspend fun insertAll(
        actions: List<AppBulkAction>
    ) {
        if (actions.isEmpty()) return

        dao.insertAll(
            actions.map { it.toEntity() }
        )
    }

    override suspend fun getRecent(
        limit: Int
    ): List<AppBulkAction> {
        return dao.getRecent(limit).toDomainList()
    }

    override suspend fun getTotalCount(): Int {
        return dao.getTotalCount()
    }

    override suspend fun deleteBefore(
        timestamp: Long
    ) {
        dao.deleteBefore(timestamp)
    }

    override suspend fun deleteById(
        id: String
    ) {
        dao.deleteById(id)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
