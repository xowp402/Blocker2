package com.blocker.core.data.repository

import com.blocker.core.common.AppDetectionEvent
import com.blocker.core.data.local.dao.AppDetectionDao
import com.blocker.core.data.mapper.AppDetectionMapper.toDomain
import com.blocker.core.data.mapper.AppDetectionMapper.toDomainList
import com.blocker.core.data.mapper.AppDetectionMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * پیاده‌سازی مخزن رویدادهای تشخیص.
 */
class AppDetectionRepositoryImpl(
    private val dao: AppDetectionDao
) : AppDetectionRepository {

    override fun observeAll(): Flow<List<AppDetectionEvent>> {
        return dao.observeAll().map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeByPackage(
        packageName: String
    ): Flow<List<AppDetectionEvent>> {
        return dao.observeByPackage(packageName).map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeByMatchedValue(
        matchedValue: String
    ): Flow<List<AppDetectionEvent>> {
        return dao.observeByMatchedValue(matchedValue).map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getRecent(
        limit: Int
    ): List<AppDetectionEvent> {
        return dao.getRecent(limit).toDomainList()
    }

    override suspend fun insert(
        event: AppDetectionEvent
    ) {
        dao.insert(event.toEntity())
    }

    override suspend fun insertAll(
        events: List<AppDetectionEvent>
    ) {
        if (events.isEmpty()) return

        dao.insertAll(
            events.map { it.toEntity() }
        )
    }

    override suspend fun getTotalCount(): Int {
        return dao.getTotalCount()
    }

    override suspend fun getCountByPackage(
        packageName: String
    ): Int {
        return dao.getCountByPackage(packageName)
    }

    override suspend fun getCountByMatchedValue(
        matchedValue: String
    ): Int {
        return dao.getCountByMatchedValue(matchedValue)
    }

    override suspend fun getCountBetween(
        startTime: Long,
        endTime: Long
    ): Int {
        return dao.getCountBetween(
            startTime = startTime,
            endTime = endTime
        )
    }

    override suspend fun getLastDetectionTime(
        matchedValue: String
    ): Long? {
        return dao.getLastDetectionTime(matchedValue)
    }

    override suspend fun deleteBefore(
        timestamp: Long
    ) {
        dao.deleteBefore(timestamp)
    }

    override suspend fun deleteByPackage(
        packageName: String
    ) {
        dao.deleteByPackage(packageName)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
