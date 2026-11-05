package com.blocker.core.data.repository

import com.blocker.core.common.AppUsageInfo
import com.blocker.core.data.local.dao.AppUsageDao
import com.blocker.core.data.mapper.AppUsageMapper.toDomain
import com.blocker.core.data.mapper.AppUsageMapper.toDomainList
import com.blocker.core.data.mapper.AppUsageMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * پیاده‌سازی مخزن آمار استفاده از برنامه‌ها.
 */
class AppUsageRepositoryImpl(
    private val dao: AppUsageDao
) : AppUsageRepository {

    override fun observeAll(): Flow<List<AppUsageInfo>> {
        return dao.observeAll().map { entities ->
            entities.toDomainList()
        }
    }

    override fun observeByPackage(
        packageName: String
    ): Flow<AppUsageInfo?> {
        return dao.observeByPackage(packageName).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun getByPackage(
        packageName: String
    ): AppUsageInfo? {
        return dao.getByPackage(packageName)?.toDomain()
    }

    override suspend fun save(
        usageInfo: AppUsageInfo
    ) {
        dao.upsert(usageInfo.toEntity())
    }

    override suspend fun saveAll(
        usageInfos: List<AppUsageInfo>
    ) {
        dao.upsertAll(
            usageInfos.map { it.toEntity() }
        )
    }

    override suspend fun addUsageTime(
        packageName: String,
        durationMillis: Long
    ) {
        if (durationMillis <= 0L) return

        val current = getByPackage(packageName) ?: return

        save(
            current.copy(
                totalUsageMillis =
                    current.totalUsageMillis + durationMillis,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun updateLastUsed(
        packageName: String,
        timestamp: Long
    ) {
        val current = getByPackage(packageName) ?: return

        save(
            current.copy(
                lastUsedAt = timestamp,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun incrementWordDetection(
        packageName: String
    ) {
        val current = getByPackage(packageName) ?: return

        save(
            current.copy(
                wordDetectionCount =
                    current.wordDetectionCount + 1,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun incrementDomainDetection(
        packageName: String
    ) {
        val current = getByPackage(packageName) ?: return

        save(
            current.copy(
                domainDetectionCount =
                    current.domainDetectionCount + 1,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun incrementBlockCount(
        packageName: String,
        blockedAt: Long
    ) {
        val current = getByPackage(packageName) ?: return

        save(
            current.copy(
                blockCount = current.blockCount + 1,
                lastBlockedAt = blockedAt,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun delete(
        packageName: String
    ) {
        dao.deleteByPackage(packageName)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
