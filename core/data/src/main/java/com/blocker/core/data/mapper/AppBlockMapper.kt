package com.blocker.core.data.mapper

import com.blocker.core.common.AppBlockRecord
import com.blocker.core.data.local.entity.AppBlockEntity

/**
 * تبدیل اطلاعات مسدودسازی برنامه‌ها
 * بین مدل دامنه و دیتابیس.
 */
object AppBlockMapper {

    /**
     * تبدیل Entity دیتابیس به مدل اصلی.
     */
    fun AppBlockEntity.toDomain(): AppBlockRecord {
        return AppBlockRecord(
            id = id,
            packageName = packageName,
            appName = appName,
            reason = reason,
            matchedValue = matchedValue,
            startedAt = startedAt,
            expiresAt = expiresAt,
            completedAt = completedAt,
            status = status
        )
    }

    /**
     * تبدیل مدل اصلی به Entity دیتابیس.
     */
    fun AppBlockRecord.toEntity(): AppBlockEntity {
        return AppBlockEntity(
            id = id,
            packageName = packageName,
            appName = appName,
            reason = reason,
            matchedValue = matchedValue,
            startedAt = startedAt,
            expiresAt = expiresAt,
            completedAt = completedAt,
            status = status
        )
    }

    /**
     * تبدیل فهرست Entityها به مدل‌های اصلی.
     */
    fun List<AppBlockEntity>.toDomainList(): List<AppBlockRecord> {
        return map { it.toDomain() }
    }

    /**
     * تبدیل فهرست مدل‌های اصلی به Entityها.
     */
    fun List<AppBlockRecord>.toEntityList(): List<AppBlockEntity> {
        return map { it.toEntity() }
    }
}
