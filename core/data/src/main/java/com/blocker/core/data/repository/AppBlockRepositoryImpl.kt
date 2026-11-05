package com.blocker.core.data.repository

import com.blocker.core.common.AppBlockRecord
import com.blocker.core.data.local.dao.AppBlockDao
import com.blocker.core.data.mapper.AppBlockMapper.toDomain
import com.blocker.core.data.mapper.AppBlockMapper.toDomainList
import com.blocker.core.data.mapper.AppBlockMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * پیاده‌سازی مخزن مسدودسازی برنامه‌ها.
 */
class AppBlockRepositoryImpl(
    private val dao: AppBlockDao
) : AppBlockRepository {

    override fun observeAll(): Flow<List<AppBlockRecord>> {
        return dao.observeAll().map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeActive(): Flow<List<AppBlockRecord>> {
        return dao.observeActive().map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeByPackage(
        packageName: String
    ): Flow<List<AppBlockRecord>> {
        return dao.observeByPackage(packageName).map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getById(
        id: String
    ): AppBlockRecord? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun getActiveByPackage(
        packageName: String,
        currentTime: Long
    ): AppBlockRecord? {
        return dao.getActiveByPackage(
            packageName = packageName,
            currentTime = currentTime
        )?.toDomain()
    }

    override suspend fun create(
        record: AppBlockRecord
    ) {
        dao.upsert(record.toEntity())
    }

    override suspend fun createAll(
        records: List<AppBlockRecord>
    ) {
        dao.upsertAll(
            records.map { it.toEntity() }
        )
    }

    override suspend fun updateExpiration(
        id: String,
        newExpiresAt: Long?
    ) {
        dao.updateExpiration(
            id = id,
            newExpiresAt = newExpiresAt
        )
    }

    override suspend fun complete(
        id: String,
        completedAt: Long
    ) {
        dao.updateStatus(
            id = id,
            status = "COMPLETED",
            completedAt = completedAt
        )
    }

    override suspend fun getExpired(
        currentTime: Long
    ): List<AppBlockRecord> {
        return dao.getExpired(currentTime).toDomainList()
    }

    override suspend fun expireAll(
        currentTime: Long
    ) {
        dao.expireAll(currentTime)
    }

    override suspend fun getActiveForRecovery(
        currentTime: Long
    ): List<AppBlockRecord> {
        return dao.getActiveForRecovery(
            currentTime
        ).toDomainList()
    }

    override suspend fun getActiveCount(
        currentTime: Long
    ): Int {
        return dao.getActiveCount(currentTime)
    }

    override suspend fun deleteBefore(
        timestamp: Long
    ) {
        dao.deleteBefore(timestamp)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
