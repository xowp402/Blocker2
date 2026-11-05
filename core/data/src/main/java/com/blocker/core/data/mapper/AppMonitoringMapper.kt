package com.blocker.core.data.mapper

import com.blocker.core.common.AppMonitoringConfig
import com.blocker.core.data.local.entity.AppMonitoringEntity

/**
 * تبدیل اطلاعات نظارت بر برنامه‌ها
 * بین مدل دامنه و دیتابیس.
 */
object AppMonitoringMapper {

    fun AppMonitoringEntity.toDomain(): AppMonitoringConfig {
        return AppMonitoringConfig(
            packageName = packageName,
            isEnabled = isEnabled,
            detectionMethods = detectionMethods,
            updatedAt = updatedAt
        )
    }

    fun AppMonitoringConfig.toEntity(): AppMonitoringEntity {
        return AppMonitoringEntity(
            packageName = packageName,
            isEnabled = isEnabled,
            detectionMethods = detectionMethods,
            updatedAt = updatedAt
        )
    }

    fun List<AppMonitoringEntity>.toDomainList(): List<AppMonitoringConfig> {
        return map { it.toDomain() }
    }

    fun List<AppMonitoringConfig>.toEntityList(): List<AppMonitoringEntity> {
        return map { it.toEntity() }
    }
}
