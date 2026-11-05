package com.blocker.core.data.repository

import com.blocker.core.common.AppMonitoringConfig
import com.blocker.core.data.local.dao.AppMonitoringDao
import com.blocker.core.data.mapper.AppMonitoringMapper.toDomain
import com.blocker.core.data.mapper.AppMonitoringMapper.toDomainList
import com.blocker.core.data.mapper.AppMonitoringMapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * پیاده‌سازی مخزن مدیریت برنامه‌های تحت نظارت.
 */
class AppMonitoringRepositoryImpl(
    private val dao: AppMonitoringDao
) : AppMonitoringRepository {

    override fun observeAll(): Flow<List<AppMonitoringConfig>> {
        return dao.observeAll().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun getByPackage(
        packageName: String
    ): AppMonitoringConfig? {
        return dao.getByPackage(packageName)?.toDomain()
    }

    override suspend fun save(
        config: AppMonitoringConfig
    ) {
        dao.upsert(config.toEntity())
    }

    override suspend fun saveAll(
        configs: List<AppMonitoringConfig>
    ) {
        dao.upsertAll(configs.map { it.toEntity() })
    }

    override suspend fun setEnabled(
        packageName: String,
        enabled: Boolean
    ) {
        val existing = dao.getByPackage(packageName)
            ?: return

        dao.upsert(
            existing.copy(
                isEnabled = enabled,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun delete(
        packageName: String
    ) {
        dao.deleteByPackage(packageName)
    }
}
