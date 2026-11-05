package com.blocker.core.data.mapper

import com.blocker.core.common.AppUsageInfo
import com.blocker.core.data.local.entity.AppUsageEntity

/**
 * تبدیل اطلاعات مصرف برنامه‌ها
 * بین مدل اصلی و دیتابیس.
 */
object AppUsageMapper {

    /**
     * تبدیل Entity دیتابیس به مدل اصلی.
     */
    fun AppUsageEntity.toDomain(): AppUsageInfo {
        return AppUsageInfo(
            packageName = packageName,
            appName = appName,
            lastUsedAt = lastUsedAt,
            totalUsageMillis = totalUsageMillis,
            wordDetectionCount = wordDetectionCount,
            domainDetectionCount = domainDetectionCount,
            blockCount = blockCount,
            lastBlockedAt = lastBlockedAt,
            updatedAt = updatedAt
        )
    }

    /**
     * تبدیل مدل اصلی به Entity دیتابیس.
     */
    fun AppUsageInfo.toEntity(): AppUsageEntity {
        return AppUsageEntity(
            packageName = packageName,
            appName = appName,
            lastUsedAt = lastUsedAt,
            totalUsageMillis = totalUsageMillis,
            wordDetectionCount = wordDetectionCount,
            domainDetectionCount = domainDetectionCount,
            blockCount = blockCount,
            lastBlockedAt = lastBlockedAt,
            updatedAt = updatedAt
        )
    }

    /**
     * تبدیل فهرست Entityها به مدل‌های اصلی.
     */
    fun List<AppUsageEntity>.toDomainList(): List<AppUsageInfo> {
        return map { it.toDomain() }
    }

    /**
     * تبدیل فهرست مدل‌های اصلی به Entityها.
     */
    fun List<AppUsageInfo>.toEntityList(): List<AppUsageEntity> {
        return map { it.toEntity() }
    }
}
